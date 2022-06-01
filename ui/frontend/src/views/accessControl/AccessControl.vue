<template>
  <div>
    <h3 class="content__title">데이터 접근제어 관리</h3>
    <SmartSearch
        :is-text="true"
        button-name="상세검색"
        @smart-search="showSmartSearch"
    />
    <p class="text__total">총 {{ totalCount }}건</p>
    <AppTable
        :meta-data="tableFields"
        :table-items="accessControls"
        @on-row-event="onDetailView"
    >
      <template v-slot:pagination>
        <AppPagination
            :total-count="totalCount"
            :pagination-value="15"
            :items="accessControls"
            @on-page-click="getAccessControlList"
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
            @add-event="onDataTableAdd"
            @del-event="onDataTableDel"
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
 * Access Control list view page (container)
 */
import AppTable from '@/components/AppTable';
import AppPagination from '@/components/AppPagination';
import AppButtons from '@/components/AppButtons';
import AppModal from '@/components/AppModal';
import SmartSearch from '@/components/SmartSearch';
import AppForm from '@/components/AppForm';
import { APIHandler } from '@/modules/api-handler';
import { mapMutations, mapState } from 'vuex';
import { dateFormat, errorRender } from '@/modules/utils';

export default {
  name: 'AccessControl',
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
      formFields: [
        [{ name: 'resourceId', displayName: '리소스 ID', type: 'text', require: false },
          { name: 'resourceType', displayName: '리소스 유형', type: 'choice', require: false,
            choices: [
              { value: 'DATASET', displayName: 'DATASET' }
            ]}
        ],
        [{ name: 'userId', displayName: '사용자 ID', type: 'text', require: false },
          { name: 'clientId', displayName: 'Client ID', type: 'text', require: false }]
      ],
      tableFields: [
        { name: 'id', displayName: '접근제어 ID', require: false, col: 15 },
        { name: 'resourceId', displayName: '리소스 ID', require: false, col: 15 },
        { name: 'resourceType', displayName: '리소스 유형', require: false, col: 10 },
        { name: 'userId', displayName: '사용자 ID', require: false, col: 10 },
        { name: 'condition', displayName: 'condition', require: false, col: 15 },
        { name: 'operation', displayName: 'operation', require: false, col: 10 },
        { name: 'createdAt', displayName: '생성시간', require: false, col: 15 },
        { name: 'modifiedAt', displayName: '수정시간', require: false, col: 15 },
      ],
      accessControls: [],
      formData: { resourceId: '', resourceType: '', userId: '', clientId: '' },
      totalCount: 0,
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
      this.formData = { resourceId: '', resourceType: '', userId: '', clientId: '' };
    },
    onCreate() {
      this.$router.push({
        name: 'AccessControlMod',
        query: {
          mode: 'add'
        }
      });
    },
    onSearch() {
      this.isShow = false;
      // this.setDataSetInfoSearchData(this.formData);
      this.getAccessControlList('search');
    },
    onDataTableAdd(data) {
      console.log(data);
      // const { value } = data;
      // this.formData.dataStoreUri.push(value);
    },
    onDataTableDel(data) {
      const { value } = data;
      this.formData.dataStoreUri.some((item, index) => {
        if (item === value) {
          this.formData.dataStoreUri.splice(index, 1);
        }
      });
    },
    getAccessControlList(searchType, pageObj) {
      let mergeObj = null;
      if (pageObj) {
        mergeObj = Object.assign(this.formData, pageObj);
      } else {
        this.formData.limit = 15;
        this.formData.offset = 0;
      }
      let queryStr = 'acl/rules?';
      queryStr += Object.entries(this.formData).map(e => e.join('=')).join('&');

      this.$http.get(APIHandler.buildUrl([queryStr]))
          .then(response => {
            const items = response.data.aclRuleResponseVOs;
            const totalCnt = response.data.totalCount;
            if (items && items !== '') {
              this.accessControls = items.map(item => {
                return {
                  id: item.id,
                  resourceId: item.resourceId,
                  resourceType: item.resourceType,
                  userId: item.userId,
                  condition: item.condition.toString(),
                  operation: item.operation,
                  createdAt: item.createdAt,
                  modifiedAt: item.modifiedAt,
                }
              });
              this.totalCount = totalCnt;
              // this.setDataSetList(items);
            } else {
              this.accessControls = [];
              this.totalCount = 0;
              // this.setDataSetList([]);
            }
          }).catch(error => {
            const result = errorRender(error.response.status, error.response.data);
            this.isAlertShow = result.isAlertShow;
            this.modalText = result.message + `(${ error.message })`;
      });
    },
    onDetailView(item) {
      this.$router.push({
        name: 'AccessControlMod',
        query: {
          id: item.id,
          mode: 'mod'
        }
      });
    }
  },
  mounted() {
    // this.formData = this.dataSetInfoSearchData;
    this.setDataModelSearchData({});
    this.setDataSetFlowSearchData({});
    this.setVerificationHistorySearchData({});
    this.setProvisionSearchData({});
    this.getAccessControlList();
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