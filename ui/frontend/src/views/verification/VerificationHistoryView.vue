<template>
  <div>
    <h3 class="content__title">품질 검증 이력</h3>
    <SmartSearch
        :is-text="true"
        button-name="상세검색"
        @smart-search="showSmartSearch"
    />
    <p class="text__total text__red">
      전체 {{ successCount + failureCount }}건, 정상 {{ successCount }}건, 비정상 {{ failureCount }}건
    </p>
    <AppTable
        :meta-data="tableFields"
        :table-items="historyList"
        @on-row-event="onDetailView"
    >
      <template v-slot:pagination>
        <AppPagination
            :total-count="totalCount"
            :pagination-value="15"
            :items="historyList"
            @on-page-click="getVerificationHistory"
        />
      </template>
    </AppTable>
    <AppModal
        :is-show="isShow"
        @close-modal="onClose"
        @on-event-modal="onSearch"
        title="상세 조건 검색"
        button-name="검색"
        :is-success-btn="true"
        :is-cancel-btn="true"
    >
      <template v-slot:elements>
        <AppForm
            title="기본 정보"
            :meta-data="formFields"
            :form-data="formData"
        />
      </template>
    </AppModal>
    <AppModal
        :is-show="isAlertShow"
        @close-modal="onClose"
        modalSize="w-360"
        :content="modalText"
        close-name="확인"
        :isCancelBtn="true"
    />
  </div>
</template>

<script>
/**
 * Verification History list view page (container)
 */
import AppForm from '@/components/AppForm';
import AppTable from '@/components/AppTable';
import SmartSearch from '@/components/SmartSearch';
import AppModal from '@/components/AppModal';
import AppPagination from '@/components/AppPagination';
import AppButtons from '@/components/AppButtons';
import * as Fields from '@/modules/meta-fields';
import {APIHandler} from "@/modules/api-handler";
import {dateFormat, errorRender, getDefaultDateRange} from '@/modules/utils';
import {mapMutations, mapState} from "vuex";


export default {
  name: 'VerificationHistoryView',
  components: {
    AppForm,
    AppTable,
    AppPagination,
    AppModal,
    AppButtons,
    SmartSearch
  },
  data() {
    return {
      isShow: false,
      isAlertShow: false,
      modalText: null,
      formFields: Fields.VERIFICATION_HISTORY_SEARCH_FIELDS,
      tableFields: Fields.VERIFICATION_HISTORY_TABLE_FIELDS,
      historyList: [],
      backupData: {},
      successCount: 0,
      failureCount: 0,
      formData: {},
      totalCount: 0
    }
  },
  computed: {
    ...mapState('searchData', [
      'verificationHistorySearchData'
    ])
  },
  methods: {
    ...mapMutations('searchData', [
      'setDataModelSearchData',
      'setDataSetInfoSearchData',
      'setDataSetFlowSearchData',
      'setVerificationHistorySearchData',
      'setProvisionSearchData',
      'setExternalPlatformSearchData'
    ]),
    showSmartSearch() {
      this.isShow = true;
    },
    onClose() {
      this.isShow = false;
      this.isAlertShow = false;
    },
    onDetailView(rowItem) {
      this.$router.push({
        name: 'VerificationModifyView',
        query: {
          mode: 'mod',
          seq: rowItem.seq
        }
      });
    },
    onSearch() {
      this.isShow = false;
      this.setVerificationHistorySearchData(this.formData);
      this.getVerificationHistory('search');
    },
    getVerificationHistory(searchType, pageObj) {
      let queryStr = '';
      if (searchType === 'search') {
        queryStr = 'verificationHistory/all';

        let result = { ...this.formData };

        if (result.datetime || result.startTime || result.endTime) {
          result.startTime = dateFormat(this.formData.datetime[0], 'yyyy-MM-dd HH:mm:ss');
          result.endTime = dateFormat(this.formData.datetime[1], 'yyyy-MM-dd HH:mm:ss');
          delete result.datetime;
        } else {
          let dateRange = getDefaultDateRange();
          result.startTime = dateFormat(dateRange.startDate, 'yyyy-MM-dd HH:mm:ss');
          result.endTime = dateFormat(dateRange.endDate, 'yyyy-MM-dd HH:mm:ss');
        }

        Object.keys(result).map((key, index) => {
          if (index === 0) {
            queryStr += `?${ key }=${ result[key] }`;
          } else {
            queryStr += `&${ key }=${ result[key] }`;
          }
        });
      } else {
        let dateRange = getDefaultDateRange();
        let startTime = dateFormat(dateRange.startDate, 'yyyy-MM-dd HH:mm:ss');
        let endTime = dateFormat(dateRange.endDate, 'yyyy-MM-dd HH:mm:ss');
        queryStr = `verificationHistory/all?startTime=${ startTime }&endTime=${ endTime }`;
      }
      // limit, offset 붙이기
      if (pageObj && Object.keys(this.formData).length > 0) {
        queryStr += `&limit=${ pageObj.limit }&offset=${ pageObj.offset }`;
      } else {
        queryStr += `&limit=15&offset=0`;
      }
      this.$http.get(APIHandler
          .buildUrl([queryStr]))
          .then(response => {
            const items = response.data.verificationHistoryResponseVO;
            const totalCnt = response.data.totalCount;
            if (!items && items === '') {
              this.totalCount = 0;
              this.historyList = [];
              return null;
            }
            let result = items.verificationHistorys.map((item) => {
              return {
                seq: item.seq,
                datasetId: item.datasetId,
                dataModelType: item.dataModelType,
                dataModelVersion: item.dataModelVersion,
                entityId: item.entityId,
                isVerified: item.verified ? '정상' : '비정상',
                testTime: item.testTime
              }
            });
            this.historyList = result;
            this.successCount = items.verificationHistoryCount.successCount;
            this.failureCount = items.verificationHistoryCount.failureCount;
            this.backupData = items;
            this.totalCount = totalCnt;
          }).catch(error => {
            const result = errorRender(error.response.status, error.response.data);
            this.isAlertShow = result.isAlertShow;
            this.modalText = result.message + `(${ error.message })`;
      });
    }
  },
  mounted() {
    this.formData = this.verificationHistorySearchData;
    this.setDataModelSearchData({});
    this.setDataSetInfoSearchData({});
    this.setDataSetFlowSearchData({});
    this.setProvisionSearchData({});
    this.setExternalPlatformSearchData({});
    if (this.formData && Object.keys(this.formData).length > 0) {
      this.getVerificationHistory('search');
    }
  }
}
</script>

<style scoped>
.text__total {
  height: 25px;
  border-top: 0;
  line-height: 35px;
}
.text__red {
  color: #dc3545;
}
</style>