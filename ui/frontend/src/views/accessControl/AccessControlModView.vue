<template>
  <div>
    <h3 class="content__title">데이터 접근 제어 관리 상세</h3>
  </div>
</template>

<script>
/**
 * Dataset list view page (container)
 */
import AppTable from '@/components/AppTable';
import AppPagination from '@/components/AppPagination';
import AppButtons from '@/components/AppButtons';
import AppModal from '@/components/AppModal';
import SmartSearch from '@/components/SmartSearch';
import AppForm from '@/components/AppForm';
import * as Fields from '@/modules/meta-fields';
import { APIHandler } from '@/modules/api-handler';
import { mapMutations, mapState } from 'vuex';
import { errorRender } from "@/modules/utils";

export default {
  name: 'DatasetView',
  components: {
    AppForm,
    AppModal,
    AppButtons,
    AppPagination,
    AppTable,
    SmartSearch
  },
  data() {
    return {
      isShow: false,
      isAlertShow: false,
      modalText: null,
      formFields: Fields.DATASET_SEARCH_FIELDS,
      tableFields: Fields.DATASET_TABLE_FIELDS,
      datasetList: [],
      formData: { dataStoreUri: [] },
      totalCount: 0,
      dataModelIds: []
    }
  },
  computed: {
    ...mapState('searchData', [
      'dataSetInfoSearchData'
    ])
  },
  methods: {
    ...mapMutations('searchData', [
      'setDataModelSearchData',
      'setDataSetInfoSearchData',
      'setDataSetFlowSearchData',
      'setVerificationHistorySearchData',
      'setProvisionSearchData'
    ]),
    ...mapMutations('dataSets', ['setDataSetList']),
    showSmartSearch() {
      this.isShow = true;
    },
    onClose() {
      this.isShow = false;
      this.isAlertShow = false;
      this.formData = { dataStoreUri: [] };
    },
    onCreate() {
      this.$router.push({
        name: 'DatasetModView',
        query: {
          mode: 'add'
        }
      });
    },
    onSearch() {
      this.isShow = false;
      this.setDataSetInfoSearchData(this.formData);
      this.getDatasetList('search');
    },
    onDataTableAdd(data) {
      const { value } = data;
      this.formData.dataStoreUri.push(value);
    },
    onDataTableDel(data) {
      const { value } = data;
      this.formData.dataStoreUri.some((item, index) => {
        if (item === value) {
          this.formData.dataStoreUri.splice(index, 1);
        }
      });
    },
    getDatasetList(searchType, pageObj) {
      let mergeObj = null;
      if (pageObj) {
        mergeObj = Object.assign(this.formData, pageObj);
      } else {
        this.formData.limit = 15;
        this.formData.offset = 0;
      }
      let queryStr = 'datasets?';
      queryStr += Object.entries(this.formData).map(e => e.join('=')).join('&');

      this.$http.get(APIHandler.buildUrl([queryStr]))
          .then(response => {
            const items = response.data.dataSetResponseVO;
            const totalCnt = response.data.totalCount;
            if (items && items !== '') {
              this.datasetList = items.map((item, index) => {
                return {
                  id: item.id,
                  name: item.name,
                  updateInterval: item.updateInterval,
                  category: item.category,
                  providerOrganization: item.providerOrganization,
                  qualityCheckEnabled: item.qualityCheckEnabled === true ? '예' : '아니오',
                  createdAt: item.createdAt
                }
              });
              this.totalCount = totalCnt;
              this.setDataSetList(items);
            } else {
              this.datasetList = [];
              this.totalCount = 0;
              this.setDataSetList([]);
            }
          }).catch(error => {
            const result = errorRender(error.response.status, error.response.data);
            this.isAlertShow = result.isAlertShow;
            this.modalText = result.message + `(${ error.message })`;
      });
    },
    onDetailView(item) {
      this.$router.push({
        name: 'DatasetModView',
        query: {
          id: item.id,
          mode: 'mod'
        }
      });
    }
  },
  mounted() {
    this.formData = this.dataSetInfoSearchData;
    this.setDataModelSearchData({});
    this.setDataSetFlowSearchData({});
    this.setVerificationHistorySearchData({});
    this.setProvisionSearchData({});
    this.getDatasetList();
  }
}
</script>

<style scoped>
.text__total {
  height: 25px;
  border-top: 0;
  line-height: 35px;
}
</style>