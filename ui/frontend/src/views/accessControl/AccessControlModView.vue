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
                <th class="icon__require">접근제어 ID</th>
                <td>
                  <label :title="formData['id']">
                    <input
                        class="input__text"
                        type="text"
                        v-model="formData['id']"
                        name="id"
                        disabled
                    />
                  </label>
                </td>
                <th class="icon__require">Client ID</th>
                <td>
                  <label>
                    <input
                        :class="error['clientId'] ? `input__text error__border` : `input__text`"
                        type="text"
                        v-model="formData['clientId']"
                        name="clientId"
                        :disabled="!conditions['clientId']"
                        @blur="onFocusoutEvent"
                    />
                  </label>
                  <br>
                  <span v-show="error['clientId']" class="error__color">
                    필수 값 입니다.
                  </span>
                </td>
                <th class="icon__require">사용자 ID</th>
                <td>
                  <label>
                    <input
                        :class="error['userId'] ? `input__text error__border` : `input__text`"
                        class="input__text"
                        type="text"
                        v-model="formData['userId']"
                        name="userId"
                        :disabled="!conditions['userId']"
                        @blur="onFocusoutEvent"
                    />
                  </label>
                  <br>
                  <span v-show="error['userId']" class="error__color">
                    필수 값 입니다.
                  </span>
                </td>
              </tr>
              <tr>
                <th class="icon__require">리소스 ID</th>
                <td>
                  <label>
                    <select
                        v-model="formData['resourceId']"
                        class="input__text"
                        :style="error['resourceId'] ? `border-color: #f56c6c;` : null"
                        name="resourceId"
                        @blur="onFocusoutEvent"
                    >
                      <option
                          v-for="item in datasetList"
                          :value="item.id"
                      >
                        {{ item.id }}
                      </option>
                    </select>
                  </label>
                </td>
                <th class="icon__require">리소스 유형</th>
                <td>
                  <label>
                    <select
                        v-model="formData['resourceType']"
                        class="input__text"
                        :style="error['resourceType'] ? `border-color: #f56c6c;` : null"
                        name="resourceType"
                        @blur="onFocusoutEvent"
                    >
                      <option
                          disabled
                          value="DATASET"
                      >
                        DATASET
                      </option>
                    </select>
                  </label>
                </td>
              </tr>
              <tr>
                <th class="icon__require">생성자</th>
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
                <th class="icon__require">생성시간</th>
                <td>
                  <label>
                    <input
                        class="input__text"
                        type="text"
                        v-model="formData['createdAt']"
                        name="createdAt"
                        disabled
                    />
                  </label>
                </td>
                <th class="icon__require">프로비저닝 요청 ID</th>
                <td>
                  <label :title="formData['provisioningRequestId']">
                    <input
                        class="input__text"
                        type="text"
                        v-model="formData['provisioningRequestId']"
                        name="provisioningRequestId"
                        disabled
                    />
                  </label>
                </td>
              </tr>
              <tr>
                <th class="icon__require">수정자</th>
                <td>
                  <label>
                    <input
                        class="input__text"
                        type="text"
                        v-model="formData['modifierId']"
                        name="modifierId"
                        disabled
                    />
                  </label>
                </td>
                <th class="icon__require">수정시간</th>
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
                <th class="icon__require">프로비저닝이벤트시간</th>
                <td>
                  <label>
                    <input
                        class="input__text"
                        type="text"
                        v-model="formData['provisioningEventTime']"
                        name="provisioningEventTime"
                        disabled
                    />
                  </label>
                </td>
              </tr>

              <tr>
                <th class="icon__require">operation</th>
                <td colspan="5">
                  <label>
                    <el-checkbox-group v-model="operations">
                      <el-checkbox label="create"></el-checkbox>
                      <el-checkbox label="update"></el-checkbox>
                      <el-checkbox label="delete"></el-checkbox>
                      <el-checkbox label="retrieve"></el-checkbox>
                    </el-checkbox-group>
                  </label>
                  <span v-show="error['operation']" class="error__color">
                    필수 값 입니다.
                  </span>
                </td>
              </tr>
              <tr>
                <th class="icon__require">condition</th>
                <td colspan="2">
                  <label>
                    <el-checkbox v-model="conditions['clientId']" @change="onChange">Client ID</el-checkbox>
                    <el-checkbox v-model="conditions['userId']" @change="onChange">사용자 ID</el-checkbox>
                  </label>
                </td>
                <td>
                  <select
                      v-if="conditions.clientId && conditions.userId"
                      v-model="formData['condition']"
                      class="input__text"
                      :style="error['condition'] ? `border-color: #f56c6c;` : null"
                      name="condition"
                      @blur="onFocusoutEvent"
                  >
                    <option value="AND">
                      AND
                    </option>
                    <option value="OR">
                      OR
                    </option>
                  </select>
                  <input
                      v-else
                      class="input__text"
                      type="text"
                      disabled
                  />
                  <span v-show="error['condition']" class="error__color">
                    필수 값 입니다.
                  </span>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </section>
        <div class="button__group">
          <button
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
        :is-show="isDelAttrShow"
        @close-modal="onClose"
        @on-event-modal="onDeleteSuccess"
        modalSize="w-360"
        :content="modalText"
        button-name="확인"
        :is-success-btn="true"
        :isCancelBtn="true"
    />
    <AppModal
        :is-show="isAlertShow"
        @close-modal="onClose"
        @on-event-modal="onDeleteSuccess"
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
import {APIHandler} from '@/modules/api-handler';
import {dateFormat, errorRender} from '@/modules/utils';
import AppModal from "@/components/AppModal";

export default {
  name: 'AccessControlMod',
  components: {AppModal},
  data() {
    return {
      formData: { resourceType: 'DATASET', resourceId: '' },
      error: {
        clientId: false, userId: false, operation: false, condition: false
      },
      operations: [],
      conditions: { clientId: false, userId: false },
      isMode: '',
      datasetList: [],
      isDelAttrShow: false,
      modalText: '',
      isAlertShow: false,
    }
  },
  methods: {
    getAccessControl() {
      const { id } = this.$route.query;
      this.$http.get(APIHandler.buildUrl(['acl', 'rules', id]))
          .then(response => {
            const { data } = response;
            this.formData = data;
            this.operations = data.operation;

            if (data.condition && data.condition !== '') {
              this.conditions = { clientId: true, userId: true };
            }

          }).catch(error => {
            const result = errorRender(error.response.status, error.response.data);
            this.isAlertShow = result.isAlertShow;
            this.modalText = result.message + `(${ error.message })`;
      });
    },
    getDatasetList() {
      this.$http.get(APIHandler.buildUrl(['datasets']))
          .then(response => {
            this.datasetList = response.data.dataSetResponseVO;
            const { mode } = this.$route.query;
            if (mode === 'add') {
              this.formData.resourceId = this.datasetList[0].id;
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
    onGoBack() {
      this.$router.push({
        name: 'AccessControl'
      });
    },
    onSave() {
      const { mode, id } = this.$route.query;
      this.formData.operation = this.operations;

      Object.keys(this.error).map(key => {
        if (!this.formData[key] || this.formData[key] === '' || this.formData[key].length === 0) {
          if (key !== 'type') {
            this.error[key] = true;
          }
        }
      });
      if (this.operations.length > 0) {
        this.error['operation'] = false;
      }
      let checkResult = Object.keys(this.error).some(key => {
        return !!this.error[key];
      });
      if (checkResult) {
        return null;
      }

      if (mode === 'add') {
        // create
        this.$http.post(APIHandler.buildUrl(['acl', 'rules']), this.formData)
            .then(response => {
              const { status } = response;
              if (status === 200 || 201 || 204) {
                this.isAlertShow = true;
                this.modalText = '저장되었습니다.';
              }
            }).catch(error => {
              const result = errorRender(error.response.status, error.response.data);
              this.isAlertShow = result.isAlertShow;
              this.modalText = result.message + `(${ error.message })`;
        });
      } else {
        // modify
        this.$http.put(APIHandler.buildUrl(['acl', 'rules', id]), this.formData)
            .then(response => {
              const { status } = response;
              if (status === 200 || 201 || 204) {
                this.isAlertShow = true;
                this.modalText = '저장되었습니다.';
              }
            }).catch(error => {
              const result = errorRender(error.response.status, error.response.data);
              this.isAlertShow = result.isAlertShow;
              this.modalText = result.message + `(${ error.message })`;
        });
      }
    },
    onDelete() {
      this.isDelAttrShow = true;
      this.modalText = '삭제하시겠습니까?';
    },
    onDeleteSuccess() {
      const { id } = this.$route.query;
      this.$http.delete(APIHandler.buildUrl(['acl', 'rules', id]))
          .then(response => {
            const { status } = response;
            if (status === 204) {
              this.$router.push({
                name: 'AccessControl'
              });
            }
          }).catch(error => {
        const result = errorRender(error.response.status, error.response.data);
        this.isAlertShow = result.isAlertShow;
        this.modalText = result.message + `(${ error.message })`;
      });
    },
    onClose() {
      this.isDelAttrShow = false;
      this.isAlertShow = false;
    },
    onChange() {
      Object.keys(this.conditions)
          .some(() => this.conditions['clientId'] && this.conditions['userId']
              ? this.formData['condition'] = 'AND'
              : this.formData['condition'] = '');
    }
  },
  mounted() {
    this.getDatasetList();
    const { mode, id } = this.$route.query;
    this.isMode = mode;
    if (mode !== 'add') {
      this.getAccessControl();
    }
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