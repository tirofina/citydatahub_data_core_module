package kr.re.keti.sc.ingestinterface.common.service.security;

import kr.re.keti.sc.ingestinterface.common.code.Constants;
import kr.re.keti.sc.ingestinterface.common.code.IngestInterfaceCode;
import kr.re.keti.sc.ingestinterface.common.exception.ngsild.NgsiLdResourceNotFoundException;
import kr.re.keti.sc.ingestinterface.common.vo.security.AASUserDetailsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Authorization and Authentication service class
 */
@Service
public class AASSVC {

    @Value("#{'${security.headers.admin.value}'.split(',')}")
    private List<String> adminUserRoleList;


    public List<String> getAclResourceIds(AASUserDetailsVO aasUserDetailsVO) {
        List<String> resourceIds = aasUserDetailsVO.getResourceIds();
        String role = aasUserDetailsVO.getRole();
        if (role == null) {
            throw new AccessDeniedException("empty role");
        }
        if (adminUserRoleList.contains(role)) {
            return null;
        }
        if (resourceIds == null || resourceIds.size() == 0) {
            return null;
//            throw new AccessDeniedException("empty access dataset");
        }
        return resourceIds;
    }


    /**
     * CUD operation 에 대한 접근제어
     *
     * @param request
     * @param datasetId
     */
    public void checkCUDAccessRule(HttpServletRequest request, String datasetId) {

        Object aclAdmin = request.getAttribute(Constants.ACL_ADMIN);
        if (aclAdmin == null) {
            //admin 권한일 경우 skip
            return;
        }

        if (datasetId == null) {
            // 데이터 셋이 없는 경우, skip
            return;
        }

        boolean isAdmin = (boolean) aclAdmin;
        if (!isAdmin) {
            List<String> aclDatasetList = (List<String>) request.getAttribute(Constants.ACL_DATASET_IDS);
            if (aclDatasetList == null) {
                throw new NgsiLdResourceNotFoundException(IngestInterfaceCode.ErrorCode.NOT_EXIST_ID, "no dataset access available");
            }
            for (String aclDataset : aclDatasetList) {

                if (datasetId.equalsIgnoreCase(aclDataset)) {
                    return;
                }
            }
            throw new NgsiLdResourceNotFoundException(IngestInterfaceCode.ErrorCode.NOT_EXIST_ID, "no dataset access available");
        }
    }
}
