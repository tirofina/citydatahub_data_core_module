<template>
  <div>
    <form>
      <fieldset>
        <legend>필드셋 제목</legend>
        <!-- section-write -->
        <section class="section">
          <div class="section__header">
            <h4 class="section__title">기본 정보</h4>
          </div>
          <div class="section__content">
            <table class="table--row">
              <caption>테이블 제목</caption>
              <colgroup>
                <col style="width:150px">
                <col style="width:auto">
                <col style="width:150px">
                <col style="width:auto">
                <col style="width:150px">
                <col style="width:auto">
              </colgroup>
              <tbody>
                <tr>
                  <th class="icon__require">서버 아이디</th>
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
                  <th class="icon__require">서버유형</th>
                  <td>
                    <label>
                      <select
                          v-model="formData['type']"
                          class="input__text"
                          :style="error['type'] ? `border-color: #f56c6c;` : null"
                          name="type"
                          @blur="onFocusoutEvent"
                      >
                        <option
                            disabled
                            :value="null"
                        >
                          Please select one
                        </option>
                        <option
                            v-for="item in targetServerTypeList"
                            :value="item.codeId"
                        >
                          {{ item.codeName }}
                        </option>
                      </select>
                    </label>
                    <br>
                    <span v-show="error['type']" class="error__color">
                      필수 값 입니다.
                    </span>
                  </td>
                  <th class="icon__require">Provision URI</th>
                  <td>
                    <label>
                      <input
                          :class="error['provisionUri'] ? `input__text error__border` : `input__text`"
                          type="text"
                          name="provisionUri"
                          v-model="formData['provisionUri']"
                          @blur="onFocusoutEvent"
                      />
                    </label>
                    <br>
                    <span v-show="error['provisionUri']" class="error__color">
                      필수 값 입니다.
                    </span>
                  </td>
                </tr>
                <tr>
                  <th class="icon__require">프로토콜 유형</th>
                  <td>
                    <label>
                      <select
                          v-model="formData['provisionProtocol']"
                          class="input__text"
                          :style="error['provisionProtocol'] ? `border-color: #f56c6c;` : null"
                          name="provisionProtocol"
                          @blur="onFocusoutEvent"
                      >
                        <option
                            disabled
                            :value="null"
                        >
                          Please select one
                        </option>
                        <option
                            v-for="item in provisionProtocolList"
                            :value="item.codeId"
                        >
                          {{ item.codeName }}
                        </option>
                      </select>
                    </label>
                    <br>
                    <span v-show="error['provisionProtocol']" class="error__color">
                      필수 값 입니다.
                    </span>
                  </td>
                  <th class="icon__require">사용여부</th>
                  <td>
                    <label>
                      <select
                          v-model="formData['enabled']"
                          class="input__text"
                          :style="error['enabled'] ? `border-color: #f56c6c;` : null"
                          name="enabled"
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
                    <span v-show="error['enabled']" class="error__color">
                      필수 값 입니다.
                    </span>
                  </td>
                  <th>생성자</th>
                  <td>
                    <label>
                      <input
                          class="input__text"
                          type="text"
                          v-model="formData['creatorId']"
                          name="creatorId"
                          disabled
                      />
                    </label>
                  </td>
                </tr>
                <tr>
                  <th>생성시간</th>
                  <td>
                    <label>
                      <input class="input__text" type="text" name="createdAt" v-model="formData['createdAt']" disabled />
                    </label>
                  </td>
                  <th>수정자</th>
                  <td>
                    <label>
                      <input class="input__text" type="text" name="modifierId" v-model="formData['modifierId']" disabled />
                    </label>
                  </td>
                  <th>수정시간</th>
                  <td>
                    <label>
                      <input
                          class="input__text"
                          type="text"
                          v-model="formData['modifiedAt']"
                          name="modifiedAt"
                          disabled
                      />
                    </label>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>
        <section class="section">
          <div class="section__header">
            <h4 class="section__title">부가 정보</h4>
          </div>
          <div class="section__content">
            <table class="table--row">
              <caption>테이블 제목</caption>
              <colgroup>
                <col style="width:120px">
                <col style="width:auto">
              </colgroup>
              <tbody>
              <tr>
                <th>설명</th>
                <td>
                  <label>
                    <input
                        class="input__text"
                        type="text"
                        v-model="formData['description']"
                        name="description"
                    />
                  </label>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </section>
        <div class="button__group">
          <button
              v-if="isMode !== 'mod'"
              class="button__primary"
              type="button"
              @click="onSave"
          >
            저장
          </button>
          <button
              v-if="isMode === 'mod'"
              class="button__secondary"
              type="button"
              @click="onDelete"
          >
            삭제
          </button>
          <button
              class="button__primary"
              type="button"
              @click="onGoBack"
          >
            목록
          </button>
        </div>
      </fieldset>
    </form>
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
  </div>
</template>

<script>
/**
 * Provision Server detail view page (container)
 */
import ElementTree from '@/components/ElementTree';
import AppTable from '@/components/AppTable';
import AppModal from '@/components/AppModal';
import { APIHandler } from "@/modules/api-handler";
import {errorRender} from "@/modules/utils";

export default {
  name: 'ProvisionServerModView',
  components: {
    ElementTree,
    AppTable,
    AppModal
  },
  props: {},
  data() {
    return {
      isSaveShow: false,
      isDelShow: false,
      isAlertShow: false,
      formData: { provisionProtocol: null, type: null, enabled: null },
      error: {
        id: false, type: false, provisionUri: false, provisionProtocol: false, enabled: false
      },
      isMode: null,
      modalText: null,
      targetServerTypeList: [],
      provisionProtocolList: [],
      commonCodeList: []
    }
  },
  methods: {
    getProvisionServer() {
      const { id, mode } = this.$route.query;
      this.$http.get(APIHandler.buildUrl(['provision', 'servers', id]))
          .then(response => {
            this.formData = response.data;
          }).catch(error => {
            const result = errorRender(error.response.status, error.response.data);
            this.isAlertShow = result.isAlertShow;
            this.modalText = result.message + `(${ error.message })`;
      });
    },
    onSave() {
      Object.keys(this.error).map(key => {
        if (!this.formData[key] || this.formData[key] === '' || this.formData[key].length === 0) {
          this.error[key] = true;
        }
      });
      let checkResult = Object.keys(this.error).some(key => {
        return !!this.error[key];
      });
      if (checkResult) {
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
        name: 'ProvisionServerView'
      });
    },
    onClose() {
      this.isSaveShow = false;
      this.isDelShow = false;
      this.isAlertShow = false;
    },
    onConfirmSave() {
      const { id, mode } = this.$route.query;
      console.log(this.formData);
      if (mode === 'add') {
        this.$http.post(APIHandler.buildUrl(['provision', 'servers']), this.formData)
            .then(response => {
              const resultCode = response.status;
              if (resultCode === 200 || 201 || 204) {
                this.onGoBack();
              }
            }).catch(error => {
              const result = errorRender(error.response.status, error.response.data);
              this.isAlertShow = result.isAlertShow;
              this.modalText = result.message + `(${ error.message })`;
        });
      } else {
        this.$http.patch(APIHandler.buildUrl(['provision', 'servers', id]), this.formData)
            .then(response => {
              const resultCode = response.status;
              if (resultCode === 200 || 201 || 204) {
                this.onGoBack();
              }
            }).catch(error => {
              const result = errorRender(error.response.status, error.response.data);
              this.isAlertShow = result.isAlertShow;
              this.modalText = result.message + `(${ error.message })`;
        });
      }
    },
    onConfirmDel() {
      const { id } = this.$route.query;
      this.$http.delete(APIHandler.buildUrl(['provision', 'servers', id]))
          .then(response => {
            const resultCode = response.status;
            if (resultCode === 200 || 201 || 204) {
              this.onGoBack();
            }
          }).catch(error => {
            const result = errorRender(error.response.status, error.response.data);
            this.isAlertShow = result.isAlertShow;
            this.modalText = result.message + `(${ error.message })`;
      });
    },
    onFocusoutEvent(event) {
      const { name, value } = event.target;
      if (value !== '') {
        this.error[name] = false;
      }
    },
    getTargetServerType() {
      this.$http.get(`/code?pageSize=999&currentPage=1&codeGroupId=DC016`)
          .then(response => {
            this.targetServerTypeList = response.data.codeVOs;
          });
    },
    getProvisionProtocol() {
      this.$http.get(`/code?pageSize=999&currentPage=1&codeGroupId=DC017`)
          .then(response => {
            this.provisionProtocolList = response.data.codeVOs;
          });
    },
    getCommonCodeList() {
      this.$http.get(`/code?pageSize=999&currentPage=1&codeGroupId=DC101`)
          .then(response => {
            this.commonCodeList = response.data.codeVOs;
          });
    }
  },
  mounted() {
    const { mode } = this.$route.query;
    this.isMode = mode;
    if (mode !== 'add') {
      this.getProvisionServer();
    }
    this.getTargetServerType();
    this.getProvisionProtocol();
    this.getCommonCodeList();
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
</style>