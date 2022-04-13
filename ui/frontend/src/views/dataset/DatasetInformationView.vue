<template>
  <section class="section">
    <div class="section__header">
      <h4 class="section__title">데이터 셋</h4>
      <div class="button__group" style="margin: 0; padding-top: 5px;">
        <!--        <button-->
        <!--            class="button__primary"-->
        <!--            type="button"-->
        <!--            @click="onSave"-->
        <!--        >-->
        <!--          저장-->
        <!--        </button>-->
        <button
            v-if="isMode === 'mod'"
            class="button__secondary"
            type="button"
            @click="onDelete"
        >
          삭제
        </button>
      </div>
    </div>
    <div class="section__content">
      <table class="table--row">
        <caption>테이블 제목</caption>
        <colgroup>
          <col style="width:170px">
          <col style="width:auto">
          <col style="width:150px">
          <col style="width:auto">
          <col style="width:150px">
          <col style="width:auto">
          <col style="width:150px">
          <col style="width:auto">
        </colgroup>
        <tbody>
        <tr>
          <th class="icon__require">데이터셋 ID</th>
          <td>
            <label>
              <input
                  :class="error['id'] ? `input__text error__border` : `input__text`"
                  type="text"
                  v-model="formData['id']"
                  name="id"
                  @blur="onFocusoutEvent"
                  :disabled="isMode === 'mod'"
              />
            </label>
            <br>
            <span v-show="error['id']" class="error__color">
                필수 값 입니다.
              </span>
          </td>
          <th class="icon__require">데이터셋 이름</th>
          <td>
            <label>
              <input
                  :class="error['name'] ? `input__text error__border` : `input__text`"
                  v-model="formData['name']"
                  name="name"
                  type="text"
                  @blur="onFocusoutEvent"
              />
            </label>
            <br>
            <span v-show="error['name']" class="error__color">
                필수 값 입니다.
              </span>
          </td>
          <th class="icon__require">갱신주기</th>
          <td>
            <label>
              <input
                  :class="error['updateInterval'] ? `input__text error__border` : `input__text`"
                  type="text"
                  name="updateInterval"
                  v-model="formData['updateInterval']"
                  @blur="onFocusoutEvent"
              />
            </label>
            <br>
            <span v-show="error['updateInterval']" class="error__color">
                필수 값 입니다.
              </span>
          </td>
          <th class="icon__require">분류체계</th>
          <td>
            <label>
              <select
                  v-model="formData['category']"
                  class="input__text"
                  :style="error['category'] ? `border-color: #f56c6c;` : null"
                  name="category"
                  @blur="onFocusoutEvent"
              >
                <option
                    disabled
                    :value="null"
                >
                  Please select one
                </option>
                <option
                    v-for="item in categoryList"
                    :value="item.codeId"
                >
                  {{ item.codeName }}
                </option>
              </select>
            </label>
            <br>
            <span v-show="error['category']" class="error__color">
                필수 값 입니다.
              </span>
          </td>
        </tr>
        <tr>
          <th class="icon__require">제공기관</th>
          <td>
            <label>
              <input
                  :class="error['providerOrganization'] ? `input__text error__border` : `input__text`"
                  type="text"
                  name="providerOrganization"
                  v-model="formData['providerOrganization']"
                  @blur="onFocusoutEvent"
              />
            </label>
            <br>
            <span v-show="error['providerOrganization']" class="error__color">
                필수 값 입니다.
              </span>
          </td>
          <th class="icon__require">제공시스템</th>
          <td>
            <label>
              <input
                  :class="error['providerSystem'] ? `input__text error__border` : `input__text`"
                  type="text"
                  name="providerSystem"
                  v-model="formData['providerSystem']"
                  @blur="onFocusoutEvent"
              />
            </label>
            <br>
            <span v-show="error['providerSystem']" class="error__color">
                필수 값 입니다.
              </span>
          </td>
          <th class="icon__require">데이터가공여부</th>
          <td>
            <label>
              <select
                  class="input__text"
                  name="isProcessed"
                  v-model="formData['isProcessed']"
                  :style="error['isProcessed'] ? `border-color: #f56c6c;` : null"
                  @blur="onFocusoutEvent"
              >
                <option
                    disabled
                    :value="null"
                >
                  Please select one
                </option>
                <option
                    v-for="item in isProcessedList"
                    :value="item.codeId"
                >
                  {{ item.codeName }}
                </option>
              </select>
            </label>
            <br>
            <span v-show="error['isProcessed']" class="error__color">
                필수 값 입니다.
              </span>
          </td>
          <th rowspan="4">데이터저장위치</th>
          <td rowspan="4">
            <AppTable
                :meta-data="[]"
                :table-items="formData['dataStoreUri']"
                tableHeight="120px"
                overflowY="auto"
                @on-row-event="onTableRowEvent"
            />
          </td>
        </tr>
        <tr>
          <th class="icon__require">소유권</th>
          <td>
            <label>
              <input
                  :class="error['ownership'] ? `input__text error__border` : `input__text`"
                  type="text"
                  name="ownership"
                  v-model="formData['ownership']"
                  @blur="onFocusoutEvent"
              />
            </label>
            <br>
            <span v-show="error['ownership']" class="error__color">
                필수 값 입니다.
              </span>
          </td>
          <th class="icon__require">라이선스</th>
          <td>
            <label>
              <select
                  class="input__text"
                  name="license"
                  v-model="formData['license']"
                  :style="error['license'] ? `border-color: #f56c6c;` : null"
                  @blur="onFocusoutEvent"
              >
                <option
                    disabled
                    :value="null"
                >
                  Please select one
                </option>
                <option
                    v-for="item in licenseList"
                    :value="item.codeId"
                >
                  {{ item.codeName }}
                </option>
              </select>
            </label>
            <br>
            <span v-show="error['license']" class="error__color">
                필수 값 입니다.
              </span>
          </td>
          <th class="icon__require">제공항목</th>
          <td>
            <label>
              <input
                  :class="error['datasetItems'] ? `input__text error__border` : `input__text`"
                  type="text"
                  name="datasetItems"
                  v-model="formData['datasetItems']"
                  @blur="onFocusoutEvent"
              />
            </label>
            <br>
            <span v-show="error['datasetItems']" class="error__color">
                필수 값 입니다.
              </span>
          </td>
        </tr>
        <tr>
          <th>생성자</th>
          <td>
            <label>
              <input
                  class="input__text"
                  type="text"
                  name="creatorId"
                  v-model="formData['creatorId']"
                  disabled
              />
            </label>
          </td>
          <th>생성시간</th>
          <td>
            <label>
              <input
                  class="input__text"
                  type="text"
                  name="createdAt"
                  v-model="formData['createdAt']"
                  disabled
              />
            </label>
          </td>
          <th class="icon__require">지역범위</th>
          <td>
            <label>
              <input
                  :class="error['targetRegions'] ? `input__text error__border` : `input__text`"
                  type="text"
                  name="targetRegions"
                  v-model="formData['targetRegions']"
                  @blur="onFocusoutEvent"
              />
            </label>
            <br>
            <span v-show="error['targetRegions']" class="error__color">
                필수 값 입니다.
              </span>
          </td>
        </tr>
        <tr>
          <th>수정자</th>
          <td>
            <label>
              <input
                  class="input__text"
                  type="text"
                  name="modifierId"
                  v-model="formData['modifierId']"
                  disabled
              />
            </label>
          </td>
          <th>수정시간</th>
          <td>
            <label>
              <input
                  class="input__text"
                  type="text"
                  name="modifiedAt"
                  v-model="formData['modifiedAt']"
                  disabled
              />
            </label>
          </td>
          <th class="icon__require">품질검증여부</th>
          <td>
            <label>
              <select
                  v-model="formData['qualityCheckEnabled']"
                  class="input__text"
                  :style="error['qualityCheckEnabled'] ? `border-color: #f56c6c;` : null"
                  name="qualityCheckEnabled"
                  @blur="onFocusoutEvent"
              >
                <option
                    disabled
                    :value="null"
                >
                  Please select one
                </option>
                <option
                    v-for="item in commonCodeList"
                    :value="item.codeId"
                >
                  {{ item.codeName }}
                </option>
              </select>
            </label>
            <br>
            <span v-show="error['qualityCheckEnabled']" class="error__color">
                필수 값 입니다.
              </span>
          </td>
        </tr>
        <tr>
          <th>제공API주소</th>
          <td>
            <label>
              <input
                  class="input__text"
                  type="text"
                  name="providingApiUri"
                  v-model="formData['providingApiUri']"
              />
            </label>
          </td>
          <th>제약사항</th>
          <td>
            <label>
              <input
                  class="input__text"
                  type="text"
                  name="restrictions"
                  v-model="formData['restrictions']"
              />
            </label>
          </td>
          <th>이력저장기간(Day)</th>
          <td>
            <label>
              <input
                  class="input__text"
                  type="text"
                  name="storageRetention"
                  v-model="formData['storageRetention']"
              />
            </label>
          </td>
          <th>토픽저장기간(ms)</th>
          <td>
            <label>
              <input
                  class="input__text"
                  type="text"
                  name="topicRetention"
                  v-model="formData['topicRetention']"
              />
            </label>
          </td>
        </tr>
        <tr>
          <th>데이터셋설명</th>
          <td colspan="3">
            <label>
              <input
                  class="input__text"
                  type="text"
                  name="description"
                  v-model="formData['description']"
              />
            </label>
          </td>
          <th rowspan="5">식별자</th>
          <td rowspan="5">
            <div class="button__group" style="margin: 0 0 5px;">
              <button
                  class="button__util button__util--add material-icons"
                  type="button"
                  name="sourceDatasetIdsAdd"
                  @click="onTableAdd"
              >
                추가
              </button>
              <button
                  class="button__util button__util--remove material-icons"
                  type="button"
                  name="sourceDatasetIdsDel"
                  @click="onTableDel"
              >
                삭제
              </button>
            </div>
            <label>
              <input class="input__text" type="text" v-model="addText.sourceDatasetIds" />
            </label>
            <AppTable
                :meta-data="[]"
                :table-items="formData['sourceDatasetIds']"
                tableHeight="90px"
                overflowY="auto"
                @on-row-event="onTableRowEvent"
            />
          </td>
          <th rowspan="5">키워드</th>
          <td rowspan="5">
            <div class="button__group" style="margin: 0 0 5px;">
              <button
                  class="button__util button__util--add material-icons"
                  type="button"
                  name="keywordAdd"
                  @click="onTableAdd"
              >
                추가
              </button>
              <button
                  class="button__util button__util--remove material-icons"
                  type="button"
                  name="keywordDel"
                  @click="onTableDel"
              >
                삭제
              </button>
            </div>
            <label>
              <input class="input__text" type="text" v-model="addText.keyword" />
            </label>
            <AppTable
                :meta-data="[]"
                :table-items="formData['keyword']"
                tableHeight="90px"
                overflowY="auto"
                @on-row-event="onTableRowEvent"
            />
          </td>
        </tr>
        <tr>
          <th class="icon__require">데이터모델ID</th>
          <td>
            <label>
              <select
                  v-model="formData['dataModelId']"
                  :class="isDataModelId ? 'input__text input__disabled' : 'input__text'"
                  :style="formData['qualityCheckEnabled'] === 'true' && !formData['dataModelId'] ? `border-color: #f56c6c;` : null"
                  name="dataModelId"
                  @blur="onFocusoutEvent"
                  :disabled="isDataModelId"
              >
                <option
                    :value="null"
                >
                  Please select one
                </option>
                <option
                    v-for="value in dataModelIds"
                    :value="value"
                >
                  {{ value }}
                </option>
              </select>
            </label>
            <br>
            <span v-show="formData['qualityCheckEnabled'] === 'true' && !formData['dataModelId']" class="error__color">
                필수 값 입니다.
              </span>
          </td>
          <th>확장자</th>
          <td>
            <label>
              <input
                  class="input__text"
                  type="text"
                  name="datasetExtension"
                  v-model="formData['datasetExtension']"
              />
            </label>
          </td>
        </tr>
        <tr>
          <th>식별자유형</th>
          <td>
            <label>
              <select
                  class="input__text"
                  name="dataIdentifierType"
                  v-model="formData['dataIdentifierType']"
                  @change="onChange"
              >
                <option
                    :value="null"
                >
                  Please select one
                </option>
                <option
                    v-for="item in identifierList"
                    :value="item.codeId"
                >
                  {{ item.codeName }}
                </option>
              </select>
            </label>
          </td>
          <th></th>
          <td></td>
        </tr>
        <tr>
          <th>&nbsp;</th>
          <td>&nbsp;</td>
        </tr>
        </tbody>
      </table>
    </div>
    <AppModal
        :is-show="isSaveShow"
        @close-modal="onClose"
        @on-event-modal="onConfirmSave"
        modalSize="w-360"
        :content="modalText"
        button-name="확인"
        :is-success-btn="true"
        :isCancelBtn="true"
    />
    <AppModal
        :is-show="isDelShow"
        @close-modal="onClose"
        @on-event-modal="onConfirmDel"
        modalSize="w-360"
        :content="modalText"
        button-name="확인"
        :is-success-btn="true"
        :isCancelBtn="true"
    />
    <AppModal
        :is-show="isAlertShow"
        @close-modal="onClose"
        modalSize="w-360"
        :content="modalText"
        close-name="확인"
        :isCancelBtn="true"
    />
    <Loading
        :opacity="0.3"
        color="#0996a5"
        background-color="#4B4B4B"
        :active.sync="isLoading"
        :can-cancel="true"
        :is-full-page="fullPage"
        :height="64"
        :width="64"
    >
    </Loading>
  </section>
</template>

<script>
/**
 * Dataset Info detail view page (container)
 */
import AppTable from '@/components/AppTable';
import { APIHandler } from '@/modules/api-handler';
import AppModal from '@/components/AppModal';
import { errorRender } from '@/modules/utils';

import Loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/vue-loading.css';

export default {
  name: 'DatasetInformationModView',
  components: {
    AppTable,
    AppModal,
    Loading
  },
  props: {
    callSave: Number,
    isDataModelId: Boolean,
    isLoadData: String,
  },
  watch: {
    callSave(count) {
      if (this.formData['qualityCheckEnabled'] === 'true' && !this.formData['dataModelId']) {
        return null;
      }
      this.onSave();
    },
    isLoadData(id) {
      this.getDataset(id, 'load');
    }
  },
  data() {
    return {
      isLoading: false,
      fullPage: true,
      isSaveShow: false,
      isDelShow: false,
      isAlertShow: false,
      formData: {
        qualityCheckEnabled: null, dataIdentifierType: null, category: null, license: null,
        isProcessed: null, dataModelId: null,
      },
      error: {
        id: false, name: false, updateInterval: false, category: false, providerOrganization: false,
        providerSystem: false, isProcessed: false, ownership: false, license: false, datasetItems: false,
        targetRegions: false, qualityCheckEnabled: false
      },
      isMode: null,
      modalText: null,
      addText: { receptionIps: null, receptionDatasetIds: null, receptionClientIds: null },
      delText: null,
      commonCodeList: [],
      identifierList: [],
      categoryList: [],
      licenseList: [],
      isProcessedList: [],
      dataModelIds: []
    }
  },
  methods: {
    getDataset(id, mode) {
      this.$http.get(APIHandler.buildUrl(['datasets', id]))
          .then(response => {
            this.formData = response.data;
            // Init id, modifyAt, createAt
            if (mode && mode === 'load') {
              this.formData.id = '';
              this.formData.modifiedAt = null;
              this.formData.createdAt = null;
            }
            this.getDataModelIds();
          });
    },
    onSave() {
      let dataModelCheck = false;
      Object.keys(this.error).map(key => {
        if (key === 'qualityCheckEnabled') {
          if (this.formData[key] === null || this.formData[key] === '') {
            this.error[key] = true;
          }
        } else {
          if (!this.formData[key] || this.formData[key] === '' || this.formData[key].length === 0) {
            this.error[key] = true;
          }
        }
      });
      let checkResult = Object.keys(this.error).some(key => {
        return !!this.error[key];
      });
      if (checkResult) {
        return null;
      }
      if (dataModelCheck) {
        return null;
      }
      this.isSaveShow = true;
      this.modalText = '저장하시겠습니까?';
    },
    onDelete() {
      this.isDelShow = true;
      this.modalText = '삭제하시겠습니까?';
    },
    onGoBack() {
      this.$router.push({
        name: 'DatasetView'
      });
    },
    onFocusoutEvent(event) {
      const { name, value } = event.target;

      if (value !== '') {
        this.error[name] = false;
      }
    },
    onTableAdd(event) {
      const { name } = event.target;
      if (name === 'sourceDatasetIdsAdd') {
        if (!this.addText.sourceDatasetIds) {
          return null;
        }
        if (!this.formData.sourceDatasetIds) {
          this.formData.sourceDatasetIds = [];
        }
        this.formData.sourceDatasetIds.push(this.addText.sourceDatasetIds);
        this.addText.sourceDatasetIds = null;
      }
      if (name === 'keywordAdd') {
        if (!this.addText.keyword) {
          return null;
        }
        if (!this.formData.keyword) {
          this.formData.keyword = [];
        }
        this.formData.keyword.push(this.addText.keyword);
        this.addText.keyword = null;
      }
    },
    onTableDel(event) {
      const { name } = event.target;
      if (this.delText) {
        let data = { ...this.formData };
        if (name === 'sourceDatasetIdsDel') {
          data.sourceDatasetIds.some((item, index) => {
            if (item === this.delText) {
              data.sourceDatasetIds.splice(index, 1);
            }
          });
        }
        if (name === 'keywordDel') {
          data.keyword.some((item, index) => {
            if (item === this.delText) {
              data.keyword.splice(index, 1);
            }
          });
        }
        if (data.sourceDatasetIds.length === 0) {
          data.sourceDatasetIds = null;
        }
        this.formData = data;
        this.delText = null;
      }
    },
    onTableRowEvent(value) {
      this.delText = value;
    },
    onClose() {
      this.isSaveShow = false;
      this.isAlertShow = false;
      this.isDelShow = false;
    },
    onConfirmSave() {
      this.isSaveShow = false;
      this.isLoading = true;
      const { mode } = this.$route.query;
      const { id } = this.formData;
      if (mode === 'mod') {
        this.$http.put(APIHandler.buildUrl(['datasets', id]), this.formData)
            .then(response => {
              const resultCode = response.status;
              if (resultCode === 200 || 201 || 204) {
                this.$router.push('datasetView');
              }
            }).catch((error) => {
          this.isLoading = false;
          const result = errorRender(error.response.status, error.response.data);
          this.isAlertShow = result.isAlertShow;
          this.modalText = result.message + `(${ error.message })`;
        });
      } else {
        this.$http.post(APIHandler.buildUrl(['datasets']), this.formData)
            .then(response => {
              const resultCode = response.status;
              if (resultCode === 200 || 201 || 204) {
                this.isLoading = false;
                alert('데이터 셋 수정을 통해\n플로우 등록 해주셔야 데이터가 저장 됩니다.');
                this.$router.push('datasetView');
              }
            }).catch((error) => {
          this.isLoading = false;
          const result = errorRender(error.response.status, error.response.data);
          this.isAlertShow = result.isAlertShow;
          this.modalText = result.message + `(${ error.message })`;
        });
      }
    },
    onConfirmDel() {
      this.isDelShow = false;
      this.isLoading = true;
      const { id } = this.formData;
      this.$http.delete(APIHandler.buildUrl(['datasets', id]))
          .then(response => {
            const resultCode = response.status;
            if (resultCode === 200 || 201 || 204) {
              this.$router.push('datasetView');
            }
          }).catch((error) => {
        this.isLoading = false;
        const result = errorRender(error.response.status, error.response.data);
        this.isAlertShow = result.isAlertShow;
        this.modalText = result.message + `(${ error.message })`;
      });
    },
    onChange(event) {
      const { name, value } = event.target;
      if (value === null || value === '') {
        this.formData[name] = null;
      }
    },
    getDataModelIds() {
      this.$http.get(APIHandler.buildUrl(['datamodels', 'id']))
          .then(response => {
            this.dataModelIds = response.data;
          });
    },
    getCommonCodeList() {
      this.$http.get(`/code?pageSize=999&currentPage=1&codeGroupId=DC101`)
          .then(response => {
            this.commonCodeList = response.data.codeVOs;
          });
    },
    getIdentifierList() {
      this.$http.get(`/code?pageSize=999&currentPage=1&codeGroupId=DC011`)
          .then(response => {
            this.identifierList = response.data.codeVOs;
          });
    },
    getCategoryList() {
      this.$http.get(`/code?pageSize=999&currentPage=1&codeGroupId=DC007`)
          .then(response => {
            this.categoryList = response.data.codeVOs;
          });
    },
    getLicenseList() {
      this.$http.get(`/code?pageSize=999&currentPage=1&codeGroupId=DC010`)
          .then(response => {
            this.licenseList = response.data.codeVOs;
          });
    },
    getIsProcessedList() {
      this.$http.get(`/code?pageSize=999&currentPage=1&codeGroupId=DC008`)
          .then(response => {
            this.isProcessedList = response.data.codeVOs;
          });
    }
  },
  mounted() {
    const { mode, id } = this.$route.query;
    this.isMode = mode;
    if (mode === 'mod') {
      this.getDataset(id);
    }
    this.getIsProcessedList();
    this.getIdentifierList();
    this.getCommonCodeList();
    this.getCategoryList();
    this.getLicenseList();
    this.getDataModelIds();
  }
}
</script>

<style scoped>
.error__color {
  color: #f56c6c; font-size: 10px;
}
.error__border {
  border-color: #f56c6c;
}

.input__disabled {
  background: #f5f5f5;
  border-color: #f5f5f5;
}
</style>