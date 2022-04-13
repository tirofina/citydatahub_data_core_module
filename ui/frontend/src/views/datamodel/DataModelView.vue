<template>
  <div>
    <h3 class="content__title">데이터 모델</h3>
    <SmartSearch
        :is-text="true"
        button-name="상세검색"
        @smart-search="showSmartSearch"
    />
    <p class="text__total">총 {{ totalCount }}건</p>
    <AppTable
        :meta-data="tableFields"
        :table-items="dataModelList"
        @on-row-event="onDetailDataModel"
    >
      <template v-slot:pagination>
        <AppPagination
            :total-count="totalCount"
            :pagination-value="15"
            :items="dataModelList"
            @on-page-click="getDataModelList"
        />
      </template>
      <template v-slot:buttons>
        <div class="button__group">
          <AppButtons
              button-name="등록"
              @on-button-event="onCreate"
          />
        </div>
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
 * Data Model list view page (container)
 */
import AppTable from '@/components/AppTable';
import AppPagination from '@/components/AppPagination';
import AppButtons from '@/components/AppButtons';
import AppModal from '@/components/AppModal';
import SmartSearch from '@/components/SmartSearch';
import AppForm from "@/components/AppForm";
import {APIHandler} from '@/modules/api-handler';
import * as Fields from '@/modules/meta-fields';
import {mapMutations, mapState} from 'vuex';
import {errorRender} from "@/modules/utils";


export default {
  name: 'DataModelView',
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
      formFields: Fields.DATAMODEL_FORM_FIELDS,
      tableFields: Fields.DATAMODEL_TABLE_FIELDS,
      dataModelList: [],
      formData: {},
      totalCount: 0
    }
  },
  computed: {
    ...mapState('searchData', [
      'dataModelSearchData'
    ]),
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
    ...mapMutations('dataModels', ['setDataModelList']),
    getDataModelList(searchType, pageObj) {
      let mergeObj = null;
      if (pageObj) {
        mergeObj = Object.assign(this.formData, pageObj);
      } else {
        this.formData.limit = 15;
        this.formData.offset = 0;
      }

      let queryStr = 'datamodels/list?';
      queryStr += Object.entries(this.formData).map(e => e.join('=')).join('&');
      this.$http.get(APIHandler.buildUrl([queryStr]))
          .then(response => {
            const items = response.data.dataModelResponseVOs;
            const totalCnt = response.data.totalCount;
            if (items && items !== '') {
              this.dataModelList = items.map(item => {
                return {
                  id: item.id,
                  typeUri: item.typeUri,
                  type: item.type,
                  creatorId: item.creatorId,
                  createdAt: item.createdAt,
                }
              });
              this.totalCount = totalCnt;
              this.setDataModelList(items);
            } else {
              this.dataModelList = [];
              this.totalCount = 0;
              this.setDataModelList([]);
            }
          }).catch(error => {
            const result = errorRender(error.response.status, error.response.data);
            this.isAlertShow = result.isAlertShow;
            this.modalText = result.message + `(${ error.message })`;
      });
    },
    showSmartSearch() {
      this.isShow = true;
    },
    onClose() {
      this.formData = {};
      this.isShow = false;
      this.isAlertShow = false;
    },
    onSearch() {
      this.isShow = false;
      this.setDataModelSearchData(this.formData);
      this.getDataModelList('search');
    },
    onCreate() {
      this.$router.push({
        name: 'DataModelModView',
        query: {
          mode: 'add'
        }
      });
    },
    onDetailDataModel(item) {
      this.$router.push({
        name: 'DataModelModView',
        query: {
          id: item.id,
          mode: 'mod'
        }
      });
    }
  },
  mounted() {
    this.formData = this.dataModelSearchData;
    this.setDataSetInfoSearchData({});
    this.setDataSetFlowSearchData({});
    this.setVerificationHistorySearchData({});
    this.setProvisionSearchData({});
    this.setExternalPlatformSearchData({});
    if (this.formData && Object.keys(this.formData).length > 0) {
      this.getDataModelList('search');
    } else {
      this.getDataModelList();
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
</style>