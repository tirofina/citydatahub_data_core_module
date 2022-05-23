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
                        class="input__text"
                        type="text"
                        v-model="formData['clientId']"
                        name="clientId"
                    />
                  </label>
                </td>
                <th class="icon__require">사용자 ID</th>
                <td>
                  <label>
                    <input
                        class="input__text"
                        type="text"
                        v-model="formData['userId']"
                        name="userId"
                        disabled
                    />
                  </label>
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
                      <el-checkbox label="CREATE"></el-checkbox>
                      <el-checkbox label="RETRIEVE"></el-checkbox>
                      <el-checkbox label="UPDATE"></el-checkbox>
                      <el-checkbox label="DELETE"></el-checkbox>
                    </el-checkbox-group>
                  </label>
                </td>
              </tr>
              <tr>
                <th class="icon__require">condition</th>
                <td>
                  <label>
                    <el-checkbox-group v-model="conditions">
                      <el-checkbox label="Client ID" value="clientId"></el-checkbox>
                      <el-checkbox label="사용자 ID" value="userId"></el-checkbox>
                    </el-checkbox-group>
                  </label>
                </td>
                <td>
                  <input
                      v-if="conditions.length < 2"
                      class="input__text"
                      type="text"
                      disabled
                  />
                  <select
                      v-else
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
      error: {},
      operations: [],
      conditions: [],
      isMode: '',
      datasetList: [],
      isDelAttrShow: false,
      modalText: '',
    }
  },
  methods: {
    getAccessControl() {
      const { id } = this.$route.query;
      this.$http.get(APIHandler.buildUrl(['acl', 'rules', id]))
          .then(response => {
            console.log(response);
            this.formData = response.data;
            this.formData.createdAt = dateFormat(new Date(response.data.createdAt), 'yyyy-MM-dd HH:mm:ss');
            this.formData.modifiedAt = dateFormat(new Date(response.data.modifiedAt), 'yyyy-MM-dd HH:mm:ss');
            this.formData.provisioningEventTime = dateFormat(new Date(response.data.provisioningEventTime), 'yyyy-MM-dd HH:mm:ss');
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
      console.log(this.formData);
      if (mode === 'add') {
        // create
        this.$http.post(APIHandler.buildUrl(['acl', 'rules']), this.formData)
            .then(response => {
              console.log(response);
            }).catch(error => {
          const result = errorRender(error.response.status, error.response.data);
          this.isAlertShow = result.isAlertShow;
          this.modalText = result.message + `(${ error.message })`;
        });
      } else {
        // modify
        this.$http.put(APIHandler.buildUrl(['acl', 'rules', id]), this.formData)
            .then(response => {
              console.log(response);
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
      this.$http.delete(APIHandler.buildUrl(['acl', 'rules', id]))
          .then(response => {
            console.log(response);
          }).catch(error => {
        const result = errorRender(error.response.status, error.response.data);
        this.isAlertShow = result.isAlertShow;
        this.modalText = result.message + `(${ error.message })`;
      });
    },
    onClose() {
      this.isDelAttrShow = false;
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
.text__total {
  height: 25px;
  border-top: 0;
  line-height: 35px;
}
</style>