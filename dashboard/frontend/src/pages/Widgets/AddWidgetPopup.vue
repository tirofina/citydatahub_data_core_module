<template>
  <WidgetPopup
    isDashboard
    :popup-title="$t('widget.widgetOptions')"
    visible
    :activeName="activeName"
    :chartType="formData['chartType']"
    :visibleChartTree="visibleChartTree"
    :visibleSearchOption="visibleSearchOption"
    @close-event="onClose"
    @tab-click="tabClick"
  >
    <template v-slot:selectBox>
      <form>
        <div class="row">
          <div class="col-md-4">
            <div class="form-group">
              <label class="control-label">{{ $t('widget.widgetType') }}</label>
              <el-select
                class="mr-sm-2"
                v-model="formData['chartType']"
                :placeholder="$t('comm.select')"
                size="small"
                style="width: 100%;"
                @change="onChartTypeChange"
                :disabled="isModify"
              >
                <el-option
                  v-for="item in chartTypes"
                  :key="item.value"
                  :label="item.text"
                  :value="item.value"
                  :disabled="item.disabled"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <div class="col-md-4" v-if="display['chartType']">
            <div class="form-group">
              <label class="control-label">{{ $t('widget.dataModelID') }}</label>
              <el-select
                class="mr-sm-2"
                v-model="dataModel"
                :placeholder="$t('message.selectOption')"
                size="small"
                style="width: 100%;"
                @change="onDataModelChange"
                :disabled="dataModelDisabled['dataModelId']"
              >
                <el-option
                  v-for="item in dataModels"
                  :key="item.value"
                  :label="item.text"
                  :value="item.value"
                  :disabled="item.disabled"
                >
                </el-option>
              </el-select>
            </div>
          </div>
<!--          <div class="col-md-4" v-if="display['chartType']">-->
<!--            <div class="form-group">-->
<!--            </div>-->
<!--          </div>-->
<!--          TODO TypeURI 내용 확인 필요-->
          <div class="col-md-4" v-if="display['typeUri']">
            <div class="form-group">
              <label class="control-label">{{ $t('widget.typeUri') }}</label>
              <el-select
                class="mr-sm-2"
                v-model="typeUri"
                placeholder="Select"
                size="small"
                style="width: 100%;"
                @change="onTypeUriChange"
                :disabled="dataModelDisabled['typeUri']"
              >
                <el-option
                  v-for="item in typeUris"
                  :key="item.value"
                  :label="item.text"
                  :value="item.value"
                  :disabled="item.disabled"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <div
            class="col-md-4"
            v-if="display['entityId']"
          >
            <div class="form-group">
              <label class="control-label">{{ $t('widget.entityInstanceID') }}</label>
              <el-select
                class="mr-sm-2"
                v-model="entityId"
                :multiple="formData['dataType'] === 'last' && (['bar'].indexOf(formData['chartType']) > -1)"
                :collapse-tags="formData['dataType'] === 'last'"
                size="small"
                style="width: 100%;"
                :placeholder="$t('message.selectOption')"
                @change="onSelectedEntity"
              >
                <el-option
                  v-for="item in entityIds"
                  :key="item.value"
                  :label="item.text"
                  :value="item.value"
                  :disabled="item.disabled"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <!--          TODO 데이터 유형 -> 표시 데이터-->
          <div
            class="col-md-4"
            v-if="display['displayData']"
          >
            <div class="form-group">
              <label class="control-label">{{ $t('widget.displayData') }}</label>
              <el-select
                class="mr-sm-2"
                v-model="formData['dataType']"
                :placeholder="$t('comm.select')"
                size="small"
                style="width: 100%;"
                @change="onDataTypeChange"
                :disabled="isDataTypeDisabled"
              >
                <el-option
                  v-for="item in dataTypes"
                  :key="item.value"
                  :label="item.text"
                  :value="item.value"
                  :disabled="item.disabled"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <div
            class="col-md-4"
            v-if="display['chartTitle']"
          >
            <div class="form-group">
              <label class="control-label">{{ $t('widget.widgetTitle') }}</label>
              <b-form-input
                id="inline-form-input-name"
                class="mr-sm-2"
                v-model="formData['chartTitle']"
              />
            </div>
          </div>
          <div
            class="col-md-4"
            v-if="display['yaxisRange']"
          >
            <div class="form-group">
              <label class="control-label">{{ $t('widget.rangeOfYaxis') }}</label>
              <b-form-input
                id="inline-form-input-name"
                class="mr-sm-2"
                v-model="formData['yaxisRange']"
                :placeholder="$t('widget.minMaxAverage')"
              />
            </div>
          </div>
          <div
            class="col-md-4"
            v-if="display['timerel']"
          >
            <div class="form-group">
              <label class="control-label">{{ $t('search.timeRelation') }}</label>
              <el-select
                v-model="timerel"
                :placeholder="$t('comm.select')"
                size="small"
                style="width: 100%;"
                @change="ontimerelChange"
              >
                <el-option
                  v-for="item in dateOptions"
                  :key="item.value"
                  :label="item.text"
                  :value="item.value"
                  :disabled="item.disabled"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <div
            class="col-md-4"
            v-if="display['time']"
          >
            <div class="form-group">
              <label class="control-label">{{ $t('widget.time') }}</label>
              <el-date-picker
                v-if="timerel !== 'between'"
                v-model="time"
                type="datetime"
                :placeholder="$t('message.selectDateTime')"
                size="small"
                style="width: 100%;"
                @change="onTimeChange"
              />
              <el-date-picker
                v-else
                v-model="times"
                type="datetimerange"
                size="small"
                :start-placeholder="$t('search.startAt')"
                :end-placeholder="$t('search.endAt')"
                style="width: 100%;"
                :default-time="['00:00:00']"
                @change="onTimeChange"
              />
            </div>
          </div>

          <div
            class="col-md-4"
            v-if="display['realtimeUpdateEnabled']"
          >
            <div class="form-group">
              <label class="control-label">{{ $t('widget.autoRefresh') }}
              </label>
              <el-select
                v-model="formData['realtimeUpdateEnabled']"
                :placeholder="$t('comm.select')"
                size="small"
                style="width: 100%;"
                :disabled="isRealtimeDisabled"
              >
                <el-option
                  v-for="(item, i) in realTimeOptions"
                  :key="i"
                  :label="item.text"
                  :value="item.value"
                  :disabled="item.disabled"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <div
            class="col-md-4"
            v-if="display['chartYName']"
          >
            <div class="form-group">
              <label class="control-label">{{ $t('widget.labelOfYaxis') }}
              </label>
              <input type="text" class="form-control" v-model="formData['chartYName']"/>
            </div>
          </div>
          <div
            class="col-md-4"
            v-if="display['chartXName']"
          >
            <div class="form-group">
              <label class="control-label">{{ $t('widget.labelOfXaxis') }}</label>
              <input type="text" class="form-control" v-model="formData['chartXName']"/>
            </div>
          </div>
          <div
            class="col-md-4"
            v-if="display['updateInterval']"
          >
            <div class="form-group">
              <label class="control-label">{{ $t('widget.refreshInterval') }}</label>
              <input
                type="number"
                class="form-control"
                :placeholder="$t('message.ZeroEqualRefresh')"
                v-model="formData['updateInterval']"
              />
            </div>
          </div>
          <div
            class="col-md-4"
            v-if="display['custom_text']"
          >
            <div class="form-group">
              <label class="control-label">{{ $t('widget.textToDisplay') }}</label>
              <input
                type="text"
                class="form-control"
                :placeholder="$t('message.maxTextLength')"
                v-model="formData['extention1']"
                maxlength="100"
              />
            </div>
          </div>
          <div
            class="col-md-4"
            v-if="display['custom_text']"
          >
            <div class="form-group">
              <label class="control-label">{{ $t('widget.fontSize') }}</label>
              <el-select
                v-model="formData['extention2']"
                :placeholder="$t('comm.select')"
                size="small"
                style="width: 100%;"
              >
                <el-option
                  v-for="item in textSizes"
                  :key="item.value"
                  :label="item.text"
                  :value="item.value"
                  :disabled="item.disabled"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <div
            class="col-md-8"
            v-if="display['image']"
          >
            <div class="form-group">
              <label class="control-label">{{ $t('widget.imageFile') }}</label>
              <div>
                <el-upload
                  ref="upload"
                  action="" :auto-upload="false"
                  :file-list="imageFiles"
                  accept="image/jpeg,image/jpg,image/png"
                  :limit="1" :on-exceed="handleExceed"
                  :on-change="handleImageChange">
                  <el-button size="small" type="primary">{{ $t('widget.selectFile') }}</el-button>
                  <div slot="tip" class="el-upload__tip">{{ $t('message.fileMaxDescription') }}</div>
                </el-upload>
              </div>
            </div>
          </div>
          <div
            class="col-md-4"
            v-if="display['latestMap']"
          >
            <div class="form-group">
              <label class="control-label">{{ $t('widget.userMapLatestData') }}</label>
              <div>
                <el-select
                  v-model="formData['mapSearchConditionId']"
                  filterable
                  :placeholder="$t('message.selectMap')"
                  size="small"
                  style="margin-right: 10px;"
                  @change="getLatest"
                >
                  <el-option
                    v-for="item in latestMaps"
                    :key="item.mapSearchConditionId"
                    :label="item.mapSearchConditionName"
                    :value="item.mapSearchConditionId"
                  >
                  </el-option>
                </el-select>
              </div>
            </div>
          </div>
        </div>
      </form>
    </template>
    <template v-slot:chartTree>
      <ElementTree
        :treeData="treeData"
        @on-tree-event="onChartClick"
        nodeKey="chart"
        :checkList="{}"
      >
      </ElementTree>
    </template>
    <template v-slot:addAttr>
      <label class="control-label mb-2">{{ $t('widget.selectedAttribute') }}
      </label>
      <input
        type="text"
        class="form-control"
        v-model="formData['chartAttribute']"
        disabled
      />
      <!-- TODO only histogram -->
      <div v-if="display['chartUnit']">
        <label class="control-label mb-2">{{ $t('widget.chartUnit') }}
        </label>
        <input
          type="text"
          class="form-control"
          v-model="formData['chartUnit']"
          :disabled="isChartAttributeString"
        />
      </div>
    </template>
    <template v-slot:tree>
      <ElementTree
        :treeData="treeData"
        @on-tree-event="onTreeEvent"
        nodeKey="query"
        :checkList="dynamicQuery"
      >
      </ElementTree>
    </template>
    <template v-slot:addQuery>
      <b-form inline class="mb-4">
        <label class="mr-sm-2">ID</label>
        <b-form-input
          id="inline-form-input-name"
          class="mb-2 mr-sm-2 mb-sm-0"
          v-model="searchId"
          disabled
        ></b-form-input>
        <div class="ml-1">
          <el-button size="small" type="info" @click="addDynamicSearch">{{ $t('comm.add') }}</el-button>
          <el-button size="small" type="primary" @click="handleDynamicSearchSave">{{ $t('comm.save') }}</el-button>
        </div>
      </b-form>
      <DynamicSearch v-for="(map, index) in addList" :key="index" :formData="map" :index="index"
                     @remove="searchRemove"/>
    </template>
    <template v-slot:buttonGroup>
      <el-popover
        v-if="isModify"
        placement="top"
        width="200"
        v-model="messageVisible">
        <p style="font-size: 12px;">
          {{ $t('message.deleteCheck') }}
        </p>
        <div style="text-align: right; margin: 0">
          <el-button size="mini" type="" @click="messageVisible = false">{{ $t('comm.cancel') }}</el-button>
          <el-button type="primary" size="mini" @click="handleRemove">{{ $t('comm.ok') }}</el-button>
        </div>
        <el-button slot="reference" class="mr-2" type="danger" size="small">{{ $t('widget.deleteWidget') }}</el-button>
      </el-popover>
      <el-button class="ml-1" type="primary" @click="widgetSave" size="small">{{ $t('comm.save') }}</el-button>
    </template>
  </WidgetPopup>
</template>

<script>
/**
 * Dashboard Add/Edit Widget Popup UI
 * Components used to register or modify dashboard widgets
 *
 * @component WidgetPopup, DynamicSearch, ElementTree
 * - element-ui
 * @props props { ... }
 * @state data() { ... }
 */
import WidgetPopup from '@/components/WidgetPopup';
import ElementTree from '@/components/ElementTree';
import DynamicSearch from '@/components/DynamicSearch';
import {typeUriApi, dataModelApi, widgetApi, latestApi} from '@/moudules/apis';

import {
  chartOptions,
  barChartOptions,
  lineChartOptions
} from '@/components/Chart/Dataset';

export default {
  name: "AddWidgetPopup",
  components: {
    WidgetPopup,
    DynamicSearch,
    ElementTree
  },
  props: {
    layout: Array,
    dashboardId: String,
    editItem: Object,
  },
  computed: {
    index() {
      return this.layout.length;
    },
    visibleChartTree() {
      const chartType = this.formData['chartType'];
      return chartType && !(chartType === 'custom_text' || chartType === 'Image' || chartType === 'map_latest');
    },
    visibleSearchOption() {
      const {chartType, dataType} = this.formData;
      return chartType === 'pie' || chartType === 'donut' || (chartType === 'histogram' && dataType === 'last');
    },
    isChartAttributeString() {
      const chartAttribute = this.validation.chartAttribute;
      return !chartAttribute || typeof chartAttribute === 'string';
    }
  },
  data() {
    return {
      isEditImage: false,
      messageVisible: false,
      time: null,
      times: [],
      timerel: null,
      entityIds: [],
      entityId: null,
      typeUri: null,
      typeUris: [],
      dynamicQuery: {},
      dataModels: [],
      addList: [],
      searchId: null,
      activeName: 'first',
      dataModel: null,
      isDataTypeDisabled: false,
      isRealtimeDisabled: false,
      isModify: false,
      treeData: [],
      imageFiles: [],
      imageFile: null,
      latestMaps: [],
      latestValue: null,
      display: {
        chartType: false, chartTitle: false, chartXName: false, chartYName: false, updateInterval: false,
        realtimeUpdateEnabled: false, timerel: false, entityId: false, displayData: false, time: false,
        yaxisRange: false, custom_text: false, image: false, latestMap: false,
      },
      dataTypes: [
        {value: null, text: this.$i18n.t('message.selectOption'), disabled: true},
        {value: 'history', text: this.$i18n.t('widget.displayHistorical'), disabled: false},
        {value: 'last', text: this.$i18n.t('widget.displayLatest'), disabled: false}
      ],
      dataModelDisabled: {dataModelId: false, typeUri: false},
      formData: {
        dashboardId: null,
        widgetId: null, chartType: null, chartOrder: null, chartSize: null, dataType: null, chartTitle: null,
        updateInterval: null, realtimeUpdateEnabled: false, yaxisRange: null,
        chartAttribute: null, chartUnit: null,
        chartXName: null, chartYName: null, realTimeOption: null, q: null,
        extention1: null, extention2: null, image: null
      },
      validation: {
        chartAttribute: null,
      },
      chartTypes: [
        {value: null, text: this.$i18n.t('message.selectOption'), disabled: true},
        {value: 'donut', text: 'Donut', disabled: false},
        {value: 'bar', text: 'Bar', disabled: false},
        {value: 'pie', text: 'Pie', disabled: false},
        {value: 'line', text: 'Line', disabled: false},
        {value: 'text', text: 'Text', disabled: false},
        {value: 'boolean', text: 'Boolean', disabled: false},
        {value: 'custom_text', text: 'Text(Custom)', disabled: false},
        {value: 'Image', text: 'Image', disabled: false},
        {value: 'map_latest', text: 'Latest Map', disabled: false},
        {value: 'histogram', text: 'Histogram', disabled: false},
      ],
      dateOptions: [
        {value: null, text: this.$i18n.t('message.selectOption'), disabled: true},
        {value: 'before', text: this.$i18n.t('search.before')},
        {value: 'after', text: this.$i18n.t('search.after')},
        {value: 'between', text: this.$i18n.t('search.between')}
      ],
      realTimeOptions: [
        {value: null, text: this.$i18n.t('message.selectOption'), disabled: true},
        {value: true, text: this.$i18n.t('comm.yes')},
        {value: false, text: this.$i18n.t('comm.no')},
      ],
      textSizes: [
        {value: null, text: this.$i18n.t('message.selectOption'), disabled: true},
        {value: '10px', text: '10px'},
        {value: '20px', text: '20px'},
        {value: '30px', text: '30px'},
        {value: '40px', text: '40px'},
      ]
    }
  },
  mounted() {
    this.isEditImage = false;
    // The status should be set to true/false values only.
    this.isModify = this.editItem && Object.keys(this.editItem).length > 0;
    // Get widget information.
    if (this.isModify) {
      this.dataModelDisabled = {dataModelId: true, typeUri: true};
      const {widgetId} = this.editItem;
      this.getWidgetInfo(widgetId);
    }

    this.formData['dashboardId'] = this.dashboardId;
    this.getDataModelList();
    this.getTypUriList();
  },
  methods: {
    searchRemove(id, index) {
      const tempTree = [...this.treeData];
      this.addList.splice(index, 1);
      if (Object.keys(this.dynamicQuery).length > 0) {
        delete this.dynamicQuery[id];
      }
      this.treeData = tempTree;
    },
    addDynamicSearch() {
      if (!this.searchId) {
        return null;
      }
      if (this.addList.length > 9) {
        this.$alert('최대 10개 입력 가능합니다.', '', {
          confirmButtonText: 'OK'
        });
        return null;
      }
      this.addList.push({
        attr: this.treeRow.fullId,
        tempId: this.treeId,
        fullId: this.treeRow.fullId,
        valueType: this.treeRow.valueType,
        condition: null,
        temp: null,
        operator: null,
        value: null
      });
    },
    handleExceed() {
      this.$message.warning(`이미 등록된 파일이 있습니다. 삭제 후 재등록 가능합니다.`);
    },
    handleImageChange(file, fileList) {
      const isLt10M = file.size / 1024 / 1024 < 10;

      if (!isLt10M) {
        this.$message.error('10MB 이하의 파일만 등록 가능합니다.');
        this.imageFiles = [];
        this.imageFile = null;
      } else this.imageFile = file.raw;
    },
    handleDynamicSearchSave() {
      const tempTree = [...this.treeData];
      this.treeData = [];
      this.addList.map(item => {
        if (item.temp === 'AND') {
          item.condition = ';';
        } else {
          item.condition = '|';
        }
        this.dynamicQuery[this.treeId] = {};
      });
      this.addList.map(item => {
        Object.keys(this.dynamicQuery).map(key => {
          if (item.tempId === key) {
            this.dynamicQuery[key] = this.addList;
          }
        });
      });
      this.treeData = tempTree;
    },
    tabClick(tab, event, active) {
      this.activeName = active;
    },
    async getEntityList(dataModelId) {
      this.$http.post('/entityIds', {dataModelId: this.dataModel})
        .then(response => {
          const status = response.status;
          const items = response.data;

          if (status === 204) {
            this.entityIds = [];
            this.treeData = [];
            return false;
          }
          let result = [{value: '', text: 'all', disabled: false}];
          items.map(item => {
            return result.push({value: item, text: item, disabled: false});
          });
          this.entityIds = result;
          this.entityIds.at(0).disabled = this.formData.dataType === 'history' && this.formData.chartType === 'bar';

          return true;
        }).then(hasEntityIds => {
        if (hasEntityIds) {
          dataModelApi.attributes(dataModelId)
            .then(data => {
              this.treeData = data;
            });
        }
      })
    },
    getAttributed() {
      dataModelApi.attributes(value)
        .then(data => {
          this.treeData = data;
        });
    },
    getDataModelList() {
      dataModelApi.fetch()
        .then(data => {
          let result = [{value: null, text: this.$i18n.t('message.selectOption'), disabled: true}];
          data.map(item => {
            return result.push({value: item, text: item, disabled: false});
          });
          this.dataModels = result;
        });
    },
    getTypUriList() {
      typeUriApi.fetch()
        .then(data => {
          let result = [{value: null, text: this.$i18n.t('message.selectOption'), disabled: true}];
          data.map(item => {
            return result.push({value: item, text: item, disabled: false});
          });
          this.typeUris = result;
        });
    },
    // 2.0 dashboard latest map list
    getLatestList() {
      latestApi.fetch('latest')
        .then(data => {
          this.latestMaps = data;
        });
    },
    // 2.0 dashboard Call additional registered late map details
    getLatest(value) {
      latestApi.detail(value)
        .then(data => {
          this.formData.mapSearchConditionId = value;
        });
    },
    imageToFile(bstr, name) {
      let n = bstr.length;
      let u8arr = new Uint8Array(n);

      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }

      let file = new File([u8arr], name, {type: 'mime'});
      this.imageFile = file;
      this.imageFiles = [file];
    },
    getWidgetInfo(widgetId) {
      this.dynamicQuery = {};

      widgetApi.fetch(this.dashboardId, widgetId)
        .then(data => {
          const {chartType, entityRetrieveVO, file, extention1, dataType} = data;
          this.formData = data;

          if (chartType === 'Image') {
            this.imageFile = new Blob([file]);
            this.imageToFile(file, extention1);
          }

          if (entityRetrieveVO) {
            this.dataModel = entityRetrieveVO.dataModelId;
            this.typeUri = entityRetrieveVO.typeUri;
            this.timerel = entityRetrieveVO.timerel;

            if (entityRetrieveVO.timerel === 'between') {
              // date format
              // new Date('2022-03-04T15:00:00.000Z')
              this.times = [new Date(entityRetrieveVO.time), new Date(entityRetrieveVO.endtime)];
            } else {
              this.time = new Date(entityRetrieveVO.time);
            }

            if (['donut', 'pie'].indexOf(chartType) > -1) {
              this.entityId = null;
            } else if (['line', 'bar'].indexOf(chartType) > -1) {
              this.entityId = dataType === 'history' ? entityRetrieveVO.id : entityRetrieveVO.id.split(',');
            } else {
              this.entityId = entityRetrieveVO.id;
            }

            this.onDataModelChange(this.dataModel);

            // Changing the data type to set detailed search conditions.
            if (entityRetrieveVO.q) {
              const query = [...entityRetrieveVO.q];
              query.map(item => {
                const key = item.attr;
                if (item.condition === ';') {
                  item.temp = 'AND';
                } else {
                  item.temp = 'OR';
                }
                if (this.dynamicQuery[key]) {
                  this.dynamicQuery[key].push(item);
                } else {
                  this.dynamicQuery[key] = [item];
                }
              });
            }
          }

          this.onChartTypeChange(chartType, 1);
        });
    },
    onTreeEvent(data, node) {
      this.searchId = data.id;
      this.treeId = data.fullId;
      this.treeRow = data;
      this.treeNode = node;
      this.addList = [];
      Object.keys(this.dynamicQuery).some(key => {
        if (key === data.fullId) {
          this.addList = this.dynamicQuery[key]
        }
      });
    },
    onChartClick(data, node) {
      const {fullId, valueType} = data;
      this.formData.chartAttribute = fullId;
      this.validation.chartAttribute = valueType;
    },
    onDataTypeChange(value) {
      this.entityId = null;
      if (this.entityIds.length > 0) {
        // TODO histogram 일때??
        this.entityIds.at(0).disabled = value === 'history' && this.formData['chartType'] === 'bar';
      }
      // TODO histogram 일때 해당 값 변경에 따라 UI 변경 필요
      if (this.formData.chartType === 'histogram') {
        const displayOption = {
          chartType: true,
          typeUri: true,
          entityId: true,
          displayData: true,
          chartTitle: true,
          chartUnit: true,
        };
        if (value === 'history') {
          displayOption.time = true;
          displayOption.timerel = true;
        }
        this.initDisplay(displayOption);
      }
    },
    onTypeUriChange(value) {
      this.dataModelDisabled = {dataModelId: true, typeUri: false};
    },
    ontimerelChange(value) {
      this.time = null;
      this.isRealtimeDisabled = value !== 'after';
    },
    onTimeChange(value) {
      console.log(value);
    },
    async onDataModelChange(value) {
      if (!this.dataModelDisabled['dataModelId']) {
        this.entityId = null
      }
      await this.getEntityList(value);

      this.dataModelDisabled = {dataModelId: this.isModify, typeUri: true};
    },
    onSelectedEntity(array) {
      if (this.dataType === 'history') {
        let newArr = [];
        let text = null;
        array.some(item => {
          if (item === '') {
            text = 'ALL';
            return newArr.push(item);
          } else {
            newArr.push(item);
          }
        });

        if (text === 'ALL') {
          this.entityId = this.entityIds.map(item => item.value);
        } else {
          this.entityId = newArr;
        }
      }
    },
    onChartTypeChange(value, type) {
      // Form information exposed according to chart type.
      this.display['chartType'] = !(value === 'custom_text' || value === 'Image' || value === 'latestMap');

      // TODO 2차고도화 변경 과업에 따른 테스트 필요
      // this.isDataTypeDisabled = value !== 'bar';

      // Data to be reset.
      if (!type) {
        this.formData['chartAttribute'] = null;
        this.validation['chartAttribute'] = null;
        this.dataModel = null;
        this.entityId = null;
        this.typeUri = null;
        this.typeUris = [];
        this.dataModelDisabled = {dataModelId: false, typeUri: false};
        this.treeData = [];
      }

      // Display setting according to chart type.
      switch (value) {
        case 'donut' :
          this.initDisplay({
            chartType: true,
            chartTitle: true,
            updateInterval: true,
          });
          break;
        case 'bar' :
          this.initDisplay({
            chartType: true,
            chartTitle: true,
            chartXName: true,
            chartYName: true,
            displayData: true,
            realtimeUpdateEnabled: true,
            timerel: true,
            entityId: true,
            time: true,
            yaxisRange: true
          });
          break;
        case 'pie' :
          this.initDisplay({
            chartType: true,
            chartTitle: true,
            updateInterval: true,
          });
          break;
        case 'line' :
          this.formData.dataType = 'history';
          this.initDisplay({
            chartType: true,
            chartTitle: true,
            chartXName: true,
            chartYName: true,
            displayData: true,
            realtimeUpdateEnabled: true,
            timerel: true,
            entityId: true,
            time: true,
            yaxisRange: true
          });
          break;
        case 'text' :
          this.initDisplay({
            chartType: true,
            chartTitle: true,
            realtimeUpdateEnabled: true,
            entityId: true,
          });
          break;
        case 'boolean' :
          this.initDisplay({
            chartType: true,
            chartTitle: true,
            realtimeUpdateEnabled: true,
            entityId: true,
          });
          break;
        case 'custom_text' :
          this.initDisplay({
            custom_text: true,
            chartTitle: true,
          });
          break;
        case 'Image' :
          this.initDisplay({
            image: true,
            chartTitle: true,
          });
          break;
        case 'map_latest' :
          this.initDisplay({
            latestMap: true,
            chartTitle: true
          });
          // Call the list of stored latest maps
          this.getLatestList();
          break;
        case 'histogram' :
          this.formData.dataType = 'history';
          this.initDisplay({
            chartType: true,
            typeUri: true,
            timerel: true,
            entityId: true,
            displayData: true,
            chartTitle: true,
            time: true,
            chartUnit: true,
          });
          break;
      }
    },
    handleRemove() {
      const {widgetId} = this.editItem;
      const chartType = this.formData['chartType'];
      this.messageVisible = false;

      // delete logic
      widgetApi.delete(this.dashboardId, widgetId)
        .then(() => {
          const {widgetId, userId} = this.formData;
          if (!this.isEditImage) {
            this.$emit('remove', {chartType, widgetId, userId});
            this.onClose();
          }
        });
    },
    async editImageWidget() {
      await this.handleRemove();
      this.isModify = false;
      this.isEditImage = true;
      this.widgetSave(true);
    },
    widgetSave() {
      const query = [];
      const chartType = this.formData['chartType'];

      // start exceptions

      // 1. String value cannot be selected for chart-type widgets.
      const notPermitStrChartType = chartType === 'pie' || chartType === 'donut' || chartType === 'bar' || chartType === 'line';
      if (notPermitStrChartType && this.validation.chartAttribute === "STRING") {
        this.$alert('해당 차트 유형에서는 String 타입의 차트 값을 선택할 수 없습니다.');
        return;
      }

      // 2. Required verification of entity ID.
      if (this.display['entityId'] && this.entityId === null) {
        this.$alert('엔티티 ID를 선택해주세요.');
        return;
      }

      if (!this.isEditImage && this.isModify && chartType === 'Image') {
        this.editImageWidget();
        return;
      }

      if (chartType === 'pie' || chartType === 'donut') {
        Object.keys(this.dynamicQuery).map(key => {
          this.dynamicQuery[key].forEach(item => {
            query.push(item);
          });
        });
      }

      if (this.display['chartType']) {
        const entityId = this.entityId ? this.entityId.toString() : this.entityId;

        this.formData.entityRetrieveVO = {
          dataModelId: this.dataModel,
          typeUri: this.typeUri,
          attrs: [this.formData['chartAttribute']],
          id: entityId,
          timerel: this.timerel,
          time: this.timerel === 'between' ? this.times.at(0) : this.time,
          endtime: this.timerel === 'between' ? this.times.at(1) : null,
          q: query
        };
      } else {
        this.formData.entityRetrieveVO = null;
      }

      // api updateInterval default 0 setting
      // (Error occurs when requesting deletion of widget websocket.)
      if (!this.formData.updateInterval) {
        this.formData.updateInterval = 0;
      }

      if (chartType === 'custom_text') {
        this.formData.dataType = null;
        this.formData.chartAttribute = 'custom_text';
      } else if (chartType === 'Image') {
        this.formData.chartAttribute = 'Image';
        this.formData.extention1 = this.imageFile.name;
      }

      if (chartType === 'map_latest') {
        this.formData.chartAttribute = 'map_latest';
      }

      // Save the chart size location.
      if (this.isModify || this.isEditImage) {
        const {x, y, w, h, i} = this.editItem;
        this.formData.chartSize = JSON.stringify({
          x, y, w, h, i
        });
      } else {
        this.formData.chartSize = JSON.stringify({
          x: (this.layout.length * 2) % (this.colNum || 12),
          y: this.layout.length + (this.colNum || 12),
          w: 2,
          h: 4,
          i: this.index
        });
        this.formData.chartOrder = this.index;
      }

      // add modify logic
      if (this.isModify) {
        widgetApi.modify(this.formData)
          .then((data) => {
            const {
              chartSize,
              chartType,
              chartTitle,
              chartXName,
              chartYName,
              yaxisRange,
              userId,
              widgetId,
              mapSearchConditionId
            } = data;
            const {x, y, w, h, i} = JSON.parse(chartSize);
            const item = {
              userId, widgetId,
              x, y, w, h, i,
              chartType,
              title: chartTitle,
              chartXName, chartYName, yaxisRange,
            }
            if (item.chartType === 'bar') {
              item.options = barChartOptions(item);
            } else if (this.formData.chartType === 'line') {
              item.options = lineChartOptions(item);
            } else if (this.formData.chartType === 'custom_text') {
              item.data = {
                extention1: this.formData.extention1,
                extention2: this.formData.extention2
              };
            } else if (this.formData.chartType === 'map_latest') {
              item.mapSearchConditionId = mapSearchConditionId;
            } else {
              item.options = chartOptions(item);
            }
            this.$emit('edit', item);
            this.onClose();
          });
        return null;
      }

      if (chartType !== 'line' && chartType !== 'bar' && chartType !== 'custom_text') {
        this.formData.dataType = 'last';
      }

      // add register logic
      const file = this.imageFile;
      widgetApi.create(this.formData, file)
        .then(data => {
          const chartSize = JSON.parse(this.formData.chartSize);
          const {widgetId, userId} = data;
          const {chartType, chartTitle, chartXName, chartYName, yaxisRange, mapSearchConditionId} = this.formData;
          let item = {
            widgetId, userId,
            x: chartSize.x,
            y: chartSize.y,
            w: 2, h: 4, i: this.index,
            chartType: chartType,
            title: chartTitle,
            chartXName,
            chartYName,
            yaxisRange,
            data: null,
            options: null,
            mapSearchConditionId
          }
          if (this.isEditImage) {
            const {x, y, w, h, i} = this.editItem;
            item = {
              ...item, x, y, w, h, i
            }
          }

          if (chartType === 'bar') {
            item.options = barChartOptions(item);
          } else if (chartType === 'line') {
            item.options = lineChartOptions(item);
          } else if (chartType === 'custom_text') {
            item.data = {
              extention1: this.formData.extention1,
              extention2: this.formData.extention2
            };
          } else if (chartType === 'Image') {
            item.data = {
              file: URL.createObjectURL(file)
            }
          } else {
            item.options = chartOptions(item);
          }

          if (this.isEditImage && chartType === 'Image') {
            this.$emit('edit', item);
          } else {
            this.$emit('add', item);
          }

          // save and close
          this.onClose();
        });
    },
    async onClose() {
      this.isEditImage = false;
      this.dataModel = null;
      this.treeData = [];
      this.chartTitle = '';
      this.typeUri = null;
      this.timerel = null;
      this.time = null;
      this.activeName = 'first';
      this.entityId = null;
      this.entityIds = [];
      this.typeUris = [];
      this.dataModels = [];
      this.imageFiles = [];
      this.imageFile = null;

      this.formData = {
        dashboardId: null,
        widgetId: null, chartType: null, chartOrder: null, chartSize: null, dataType: null, chartTitle: null,
        updateInterval: null, realtimeUpdateEnabled: false, chartAttribute: null, yaxisRange: null,
        chartXName: null, chartYName: null, realTimeOption: null, q: null,
        extention1: null, extention2: null, image: null, data: null, entityRetrieveVO: null,
      };
      this.validation = {
        chartAttribute: null,
      };
      this.dataModelDisabled = {dataModelId: false, typeUri: false};
      // Initialize when the widget window is closed.
      await this.$nextTick();
      this.initDisplay();
      this.$emit('close');
    },
    initDisplay(obj = {}) {
      // only update param obj
      this.display = {
        chartType: false,
        chartTitle: false, chartXName: false, chartYName: false, updateInterval: false,
        realtimeUpdateEnabled: false, timerel: false, entityId: false, time: false, displayData: false,
        yaxisRange: false, custom_text: false, image: false, latestMap: false,
        chartUnit: false,
        ...obj,
      };
    }
  }
}
</script>

<style>
.el-upload__tip {
  font-size: 12px;
  color: red;
}

.el-upload-list {
  position: absolute;
  left: 20%;
  top: 22px;
}
</style>
