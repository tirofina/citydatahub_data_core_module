<template>
  <div>
    <h3 class="content__title">{{ $route.meta.breadcrumb[1] }}</h3>
    <AppForm
        title="기본정보"
        :meta-data="formFields"
        :form-data="formData"
    />
    <AppForm
        title="부가정보"
        :meta-data="formAdditionFields"
        :form-data="formData"
        :form-buttons="formButtons"
    />
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
 * Verification History detail view page (container)
 */
  import AppForm from '@/components/AppForm';
  import AppTable from '@/components/AppTable';
  import SmartSearch from '@/components/SmartSearch';
  import AppModal from '@/components/AppModal';
  import AppPagination from '@/components/AppPagination';
  import AppButtons from '@/components/AppButtons';
  import * as Fields from '@/modules/meta-fields';
  import { APIHandler } from '@/modules/api-handler';
  import { errorRender } from "@/modules/utils";


  export default {
  name: 'DataSetFlowView',
  components: {
    AppForm,
    AppTable,
    AppPagination,
    AppModal,
    AppButtons,
    SmartSearch,
  },
  props: {
    objData: Object
  },
  data() {
    return {
      formFields: Fields.VERIFICATION_HISTORY_FORM_FIELDS,
      formAdditionFields: [],
      formData: { sourceDatasetIds:[], keywords: [] },
      formButtons: [
        { id: 'goBack', name: '목록', className: 'button__primary', onButtonEvent: this.onGoBack, isHide: false }
      ],
      isAlertShow: false,
      modalText: null,
      selectedValue: null
    }
  },
  methods: {
    onGoBack() {
      this.formData = {};
      this.$router.push('verificationHistoryView');
    },
    getVerification(seq) {
      this.$http.get(APIHandler.buildUrl(['verificationHistory', seq]))
          .then(response => {
            this.formData = response.data;
            if (response.data.errorCode) {
              this.selectedValue = response.data.errorCode;
            }
      }).catch(error => {
        const result = errorRender(error.response.status, error.response.data);
        this.isAlertShow = result.isAlertShow;
        this.modalText = result.message + `(${ error.message })`;
      });
    },
    onClose() {
      this.isAlertShow = false;
    },
    getVerificationErrorCode() {
      this.$http.get(`/code?pageSize=999&currentPage=1&codeGroupId=DC018`)
          .then(response => {
            let items = response.data.codeVOs;
            let result = [];
            items.map(item => {
              result.push({
                value: item.codeId,
                displayName: item.codeName
              });
            });
            this.formAdditionFields = [
              [{ name: 'errorCode', displayName: '품질검사오류코드', type: 'choice',
                choices: result, selectedValue: this.selectedValue,
                require: false, isTable: false, col: 170 }],
              [{ name: 'errorCause', displayName: '상세오류메시지', require: false, type: 'text', isTable: false, col: 170 }],
              [{ name: 'data', displayName: '실패원본데이터', require: false, type: 'text', isTable: false, col: 170 }]
            ];
          });
    }
  },
  mounted() {
    const { mode, seq } = this.$route.query;
    if (mode === 'mod') {
      this.getVerification(seq);
    }
    this.getVerificationErrorCode();
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