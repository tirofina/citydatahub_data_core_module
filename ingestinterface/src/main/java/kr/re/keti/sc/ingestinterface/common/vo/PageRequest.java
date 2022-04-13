package kr.re.keti.sc.ingestinterface.common.vo;

/**
 * Page domain class
 */
public class PageRequest {

    Integer offset;
    Integer limit;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
