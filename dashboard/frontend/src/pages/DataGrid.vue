<template>
  <div class="content" style="overflow-x:hidden; height:100%;">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h4 class="card-title">Data Search</h4>
              <div class="mt-4">
                <b-form inline v-on:submit.prevent="onSubmitEvent">
                  <label class="mr-sm-2">Data Model</label>
                  <b-input-group class="mb-2 mr-sm-2 mb-sm-0">
                    <b-form-select
                      id="input-1"
                      type="text"
                      v-model="selected"
                      :options="dataModels"
                      @change="onChange"
                    ></b-form-select>
                  </b-input-group>
                  <label class="mr-sm-2">Keywords</label>
                  <b-form-input
                    id="inline-form-input-name"
                    class="mb-2 mr-sm-2 mb-sm-0"
                    placeholder="Provide a keyword."
                    v-model="searchValue"
                    :disabled="isDisabledSearch"
                  ></b-form-input>
                  <el-button size="small" type="info" @click="handleShowPopup">Options</el-button>
                  <el-button size="small" type="primary" @click="getTotalData" v-if="!isBtnLoading">Search</el-button>
                  <el-button size="small" type="primary" :loading="true" v-if="isBtnLoading">Searching</el-button>
                </b-form>
              </div>
            </div>
            <div class="card-body">
              <grid
                :data="totalGridData"
                :columns="totalColumns"
                :pageOptions="{ useClient: true, perPage: 15 }"
                :columnOptions="columnOptions"
                @mouseover="onMouseOverEvent"
                @click="onClick"
                ref="tuiGrid1"
              />
              <el-popover
                placement="top"
                width="200"
                trigger="manual"
                v-model="visible"
                :content="content"
                ref="dataGridTooltip"
              >
              </el-popover>
              <vue-simple-context-menu
                :elementId="'totalContextMenu'"
                :options="[{ name: 'Fetch Detail Entity Data', type: 1 }, { name: 'Fetch Historical Entity Data', type: 2 }]"
                :ref="'vueSimpleContextMenu'"
                @option-clicked="optionClicked"
              />
            </div>

            <div class="card-body" v-if="entityHistoryShow">
              <div class="mb-3 text-right">
                <b-form inline v-on:submit.prevent="onSubmitEvent">
                  <label class="mr-sm-2">Time Relation</label>
                  <b-input-group class="mb-2 mr-sm-2 mb-sm-0">
                    <b-form-select
                      type="text"
                      v-model="dateSelected"
                      :options="dateOptions"
                      @change="onChange2"
                    ></b-form-select>
                  </b-input-group>
                  <label class="mr-sm-2"></label>
                  <el-date-picker
                    v-show="dateSelected === 'between'"
                    v-model="dateTime"
                    type="datetimerange"
                    size="small"
                    start-placeholder="start at"
                    end-placeholder="end at"
                    class="mr-sm-2"
                    :default-time="['00:00:00']">
                  </el-date-picker>
                  <el-date-picker
                    v-show="dateSelected !== 'between'"
                    v-model="dateTime"
                    size="small"
                    type="datetime"
                    class="mr-sm-2"
                    placeholder="date, time">
                  </el-date-picker>
                  <b-form-select
                    type="text"
                    v-model="dateSelected2"
                    :options="dateOptions2"
                    class="mr-sm-2"
                    @change="onChange3"
                  ></b-form-select>
                  <el-button id="reSearch" size="small" type="primary" @click="() => getHistoryData('reSearch')">Search</el-button>
                  <el-button id="historyBtn" size="small" type="info" @click="onHistoryClose">Close</el-button>
                </b-form>
              </div>
              <grid
                :data="historyData"
                :columns="historyColumns"
                :pageOptions="{ useClient: true, perPage: 15 }"
                @mouseover="onMouseOverEvent2"
                @click="onClick2"
                ref="tuiGrid2"
              />
              <el-popover
                placement="top"
                width="200"
                trigger="manual"
                v-model="visible2"
                :content="content2"
                ref="dataGridTooltip2"
              >
              </el-popover>
              <vue-simple-context-menu
                :elementId="'historyContextMenu'"
                :options="[{ name: 'Fetch Detail Entity Data', type: 1 }]"
                :ref="'vueSimpleContextMenu2'"
                @option-clicked="optionClicked2"
              />
            </div>
          </div>
        </div>
      </div>
    </div>

    <SearchConfiguration
      :visible="dialogVisible"
      @close-event="handleClose"
    >
      <template v-slot:selectBox>
        <b-form-select
          id="input-1"
          type="text"
          required
          v-model="selected"
          :options="dataModels"
          disabled
        ></b-form-select>
      </template>
      <template v-slot:tree>
        <ElementTree :treeData="treeData" @on-tree-event="onTreeEvent" :checkList="dynamicQuery">
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
            <el-button size="small" type="info" @click="addDynamicSearch">Add</el-button>
            <el-button size="small" type="primary" @click="handleDynamicSearchSave">Save</el-button>
          </div>
        </b-form>
        <DynamicSearch v-for="(map, index) in addList" :formData="map" :index="index" @remove="searchRemove" />
      </template>
      <template v-slot:buttonGroup>
        <el-popover
          placement="top"
          width="200"
          v-model="visible3">
          <p style="font-size: 12px;">
            Do you want to reset?
          </p>
          <div style="text-align: right; margin: 0">
            <el-button size="mini" type="" @click="visible3 = false">Cancel</el-button>
            <el-button type="primary" size="mini" @click="initClose">OK</el-button>
          </div>
          <el-button slot="reference" class="mr-2" type="danger" size="small">Reset</el-button>
        </el-popover>
        <el-button class="ml-1" type="primary" @click="handleSave" size="small">Save</el-button>
      </template>
    </SearchConfiguration>
    <el-dialog
      title="Model Attribute"
      :visible.sync="dialogVisible2"
      width="30%"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <div class="mb-3">
        <b-form inline>
          <label class="mr-sm-2">ID</label>
          <b-form-input
            id="inline-form-input-name"
            class="col"
            v-model="rowEntityId"
            disabled
          />
        </b-form>
      </div>
      <div class="card">
        <div class="card-body" style="height: 20vmax; overflow-y: auto;">
          <JsonViewer
            :value="detailData"
            :expand-depth=5
            copyable
            sort
          >
          </JsonViewer>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible2 = false" size="small">OK</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
/**
 * Data Serach Page
 *
 * @component
 * - Grid, SearchConfiguration, ElementTree, DynamicSearch, JsonViewer
 * - element-ui
 * @props props { ... }
 * @state data() { ... }
 */

  import 'tui-grid/dist/tui-grid.css';
  import { Grid } from '@toast-ui/vue-grid';
  import SearchConfiguration from '../components/SearchConfiguration';
  import ElementTree from '../components/ElementTree';
  import DynamicSearch from '../components/DynamicSearch';
  import _ from 'lodash';
  import JsonViewer from 'vue-json-viewer';

  import TuiGrid from 'tui-grid';

  TuiGrid.setLanguage('ko', { display: { noData: 'No search result.' } });
  TuiGrid.applyTheme('striped', {
    cell: {
      focused: {
        border: 'none'
      },
      focusedInactive: {
        border: 'none'
      }
    },
    row: { click: { background: '#d5eaee' } },
    cell: {
      normal: {
        background: '#fff',
        border: '#fff',
        showVerticalBorder: false,
        showHorizontalBorder: true,
      }
    }
  });


  export default {
    name: 'DataGrid',
    components: {
      Grid,
      SearchConfiguration,
      ElementTree,
      DynamicSearch,
      JsonViewer
    },
    data() {
      return {
        tuiGrid: null,
        dialogVisible: false,
        dialogVisible2: false,
        entityHistoryShow: false,
        searchValue: null,
        selected: null,
        dataModels: [],
        addList: [],
        totalGridData: [],
        totalColumns:[],
        historyColumns: [],
        historyData: [],
        historyBackupData: [],
        searchData: null,
        isBtnLoading: false,
        isDisabledSearch: false,
        content: null,
        content2: null,
        visible: false,
        visible2: false,
        visible3: false,
        columnOptions: {
          minWidth: 100,
        },
        rowData: null,
        rowEntityId: null,
        detailData: {},
        treeData: [],
        searchId: null,
        treeId: null,
        treeRow: null,
        treeNode: null,
        dynamicQuery: {},
        queryApply: {},
        dateSelected: null,
        dateOptions: [
          { value: null, text: 'Please select an option', disabled: true },
          { value: 'before', text: 'before' },
          { value: 'after', text: 'after' },
          { value: 'between', text: 'between' }
        ],
        dateTime: '',
        dateSelected2: null,
        dateOptions2: [
          { value: null, text: 'Please select an option', disabled: true },
          { value: 'normalizedHistory', text: 'Full Entity Representation' },
          { value: 'temporalValues', text: 'Attributes with ObservedAt' }
        ],
      }
    },
    created() {
      this.gridProps = {
        data: {
          api: {
            readData: { url: '/entities', method: 'get' },
            contentType: "application/json",
          },
          initialRequest: false
        },
        // Options other than data and columns are defined in the options object.
        options: {
          pageOptions: {
            perPage: 1
          }
        },
        columns: [
          { name: "userId", header: "아이디", align: "center" },
          { name: "userNm", header: "이름", align: "center" },
          { name: "regDt", header: "가입일", align: "center" }
        ]
      }
    },
    methods: {
      onClick(event) {
        if (event.targetType !== 'cell') {
          return null;
        }
        event.nativeEvent.stopPropagation();
        this.$refs.dataGridTooltip.doClose();
        this.$refs.vueSimpleContextMenu.showMenu(event, event.instance.getRow(event.rowKey));
        this.$refs.vueSimpleContextMenu.$el.childNodes[0].style.fontWeight = 'bold';
        this.$refs.vueSimpleContextMenu.$el.childNodes[0].style.fontSize = '12px';
        this.$refs.vueSimpleContextMenu.$el.childNodes[0].style.top = (event.nativeEvent.pageY - 90) + 'px';
        this.$refs.vueSimpleContextMenu.$el.childNodes[0].style.left = (event.nativeEvent.pageX - 300) + 'px';

        this.rowData = this.$refs.tuiGrid1.invoke('getRow', event.rowKey);
        this.rowEntityId = this.$refs.tuiGrid1.invoke('getRow', event.rowKey).id;
      },
      onClick2(event) {
        if (event.targetType !== 'cell') {
          return null;
        }
        event.nativeEvent.stopPropagation();
        this.$refs.dataGridTooltip2.doClose();
        this.$refs.vueSimpleContextMenu2.showMenu(event, event.instance.getRow(event.rowKey));
        this.$refs.vueSimpleContextMenu2.$el.childNodes[0].style.fontWeight = 'bold';
        this.$refs.vueSimpleContextMenu2.$el.childNodes[0].style.fontSize = '12px';
        this.$refs.vueSimpleContextMenu2.$el.childNodes[0].style.top = (event.nativeEvent.pageY - 90) + 'px';
        this.$refs.vueSimpleContextMenu2.$el.childNodes[0].style.left = (event.nativeEvent.pageX - 300) + 'px';
      },
      handleClick(event, item) {
        this.$refs.vueSimpleContextMenu.showMenu(event, item);
      },
      handleClose() {
        this.dialogVisible = false;
        this.dialogVisible2 = false;
        this.treeId = null;
        this.searchId = null;
        this.treeData = [];
        this.addList = [];
        this.isDisabledSearch = false;
      },
      initClose() {
        this.addList = [];
        this.dynamicQuery = {};
        this.visible3 = false;
        this.isDisabledSearch = false;
      },
      handleShowPopup() {
        if (!this.selected) {
          this.$alert('Select Data Model(s)', '', {
            confirmButtonText: 'OK'
          });
          return null;
        }
        this.getDataModels();
        this.dialogVisible = true;
      },
      handleSave() {
        this.treeId = null;
        this.searchId = null;
        this.addList = [];
        this.dialogVisible = false;
        this.isDisabledSearch = true;
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
      handleDynamicSearchSave() {
        const tempTree = [ ...this.treeData ];
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
      optionClicked({ item, option }) {
        if (option.type === 1) {
          this.dialogVisible2 = true;
          this.getDetailData();
        } else {
          this.entityHistoryShow = true;
          this.getHistoryData();
        }
      },
      optionClicked2({ item, option }) {
        // History table detail view pop-up.
        this.detailData = {};
        this.dialogVisible2 = true;
        this.historyBackupData.map(data => {
          if (item.index === data.index) {
            Object.keys(data).map(key => {
              if (key !== 'index') {
                this.detailData[key] = data[key];
              }
            });
          }
        });
      },
      onChange() {
        this.addList = [];
        this.dynamicQuery = {};
      },
      onChange2() {
        this.dataTime = '';
        this.dateSelected2 = null;
      },
      onChange3() {

      },
      onMouseOverEvent(event) {
        event.nativeEvent.stopPropagation();
        if (event.targetType !== 'cell') {
          return null;
        }
        const data = this.$refs.tuiGrid1.invoke('getRow', event.rowKey);
        this.content = data[event.columnName];
        if (data[event.columnName] === null || data[event.columnName] === '') {
          return null;
        }
        const element = this.$refs.tuiGrid1.getRootElement().querySelectorAll('.tui-grid-cell-content');
        element.forEach(node => {
          node.title = this.content;
        });
      },
      onMouseOverEvent2(event) {
        event.nativeEvent.stopPropagation();
        if (event.targetType !== 'cell') {
          return null;
        }
        const data = this.$refs.tuiGrid2.invoke('getRow', event.rowKey);
        this.content2 = data[event.columnName];
        if (data[event.columnName] === null || data[event.columnName] === '') {
          return null;
        }
        const element = this.$refs.tuiGrid2.getRootElement().querySelectorAll('.tui-grid-cell-content');
        element.forEach(node => {
          node.title = this.content2;
        });
      },
      onHistoryClose(event) {
        event.stopPropagation();
        window.scrollTo( {top: 0, behavior: 'smooth'} );
        setTimeout(() => {
          this.$refs.tuiGrid2.invoke('setColumns', []);
          this.$refs.tuiGrid2.invoke('resetData', []);
          this.entityHistoryShow = false;
        }, 100);
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
      searchRemove(id, index) {
        const tempTree = [ ...this.treeData ];
        this.addList.splice(index, 1);
        if (Object.keys(this.dynamicQuery).length > 0) {
          delete this.dynamicQuery[id];
        }
        this.treeData = tempTree;
      },
      // axias data binding
      getDataModelList() {
        this.$http.get('/datamodelIds')
            .then((response) => {
              const items = response.data;
              let result = [{ value: null, text: 'Please select an option', disabled: true }];
              items.map(item => {
                return result.push({ value: item, text: item, disabled: false });
              });
              this.dataModels = result;
            });
      },
      onKeyDownEvent() {
        if (!this.selected) {
          return null;
        }
      },
      onSubmitEvent(event) {
        if (this.selected && this.searchValue) {
          this.getTotalData();
        }
      },
      getTotalData() {
        window.scrollTo({ top: 0, behavior: 'smooth' });
        this.entityHistoryShow = false;
        this.isBtnLoading = true;
        if (!this.selected) {
          this.$alert('데이터 모델을 선택해주세요.', '', {
            confirmButtonText: 'Confirm'
          });
          return null;
        }
        // The structure of making search terms.
        const query = [];
        Object.keys(this.dynamicQuery).map(key => {
          this.dynamicQuery[key].forEach(item => {
            query.push(item);
          });
        });
        this.searchData = { dataModelId: this.selected, searchValue: this.searchValue, q: query };
        this.$http.post('/entities', this.searchData)
          .then(response => {
            const status = response.status;
            const items = response.data;
            if (status === 204) {
              this.$refs.tuiGrid1.invoke('setColumns', []);
              this.$refs.tuiGrid1.invoke('resetData', []);
              this.isBtnLoading = false;
              return null;
            }
            // dynamic header
            this.totalColumns = [
              { header: 'Data Model ID', name: 'type', align: 'center', width: 200, resizable: true, ellipsis: true },
              { header: 'Entity ID', name: 'id', align: 'center', width: 200, resizable: true, ellipsis: true },
              { header: 'Create Date', name: 'createdAt', align: 'center', width: 250, resizable: true, ellipsis: true },
              { header: 'Modified Date', name: 'modifiedAt', align: 'center', width: 250, resizable: true, ellipsis: true }
            ];
            if (items.attrsLabel) {
              items.attrsLabel.map(item => {
                this.totalColumns.push({
                  header: item,
                  name: item,
                  align: 'center',
                  width: item.length < 30 ? 200 : 250,
                  resizable: true,
                  ellipsis: true
                });
              });
            }
            items.commonEntityVOs.map(item => {
              Object.keys(item).map((key) => {
                if (item[key].constructor === Object) {
                  return item[key] = JSON.stringify(item[key]);
                }
                if (Array.isArray(item[key])) {
                  return item[key] = JSON.stringify(item[key]);
                }
              });
            });

            this.totalGridData = items.commonEntityVOs;
            this.$refs.tuiGrid1.invoke('setColumns', this.totalColumns);
            this.$refs.tuiGrid1.invoke('resetData', this.totalGridData);
            this.isBtnLoading = false;
          });
      },
      getDetailData() {
        // Search history details.
        // if (item) {
        //   this.$http.post(`/temporal/entities/${ item.id }`, this.searchData)
        //       .then(response => {
        //         const status = response.status;
        //         if (status === 204) {
        //           return null;
        //         }
        //         this.detailData = response.data.commonEntityVO;
        //       });
        //   return null;
        // }
        // Search everything in detail.
        this.$http.post(`/entities/${ this.rowData.id }`, {})
            .then(response => {
              const status = response.status;
              if (status === 204) {
                return null;
              }
              // console.log(response.data);
              this.detailData = response.data;
            });
      },
      getHistoryData(type) {
        // Set the selected re-search conditions and hand them over to parameters.
        let params = this.searchData;
        if (type) {
          params.options = this.dateSelected2;
          params.time = this.dateSelected2 === 'between' ? this.dateTime[0] : this.dateTime;
          params.endtime = this.dateSelected2 === 'between' ? this.dateTime[1] : null
        }
        // console.log(this.searchData);
        // console.log(JSON.stringify(this.searchData));

        this.$http.post(`/temporal/entities/${this.rowData.id}`, params)
            .then(response => {
              const status = response.status;
              const items = response.data;
              if (status === 204) {
                this.$refs.tuiGrid2.invoke('setColumns', []);
                this.$refs.tuiGrid2.invoke('resetData', []);
                this.isBtnLoading = false;
                return null;
              }
              // console.log(items);
              // Backup data for detailed view.
              items.commonEntityVOs.forEach((item, index) => {
                item.index = index;
              });
              this.historyBackupData = _.cloneDeep(items.commonEntityVOs);
              this.historyColumns = [
                { header: 'Data Model ID', name: 'type', align: 'center', width: 200, resizable: true, ellipsis: true },
                { header: 'Entity ID', name: 'id', align: 'center', width: 200, resizable: true, ellipsis: true }
              ];
              if (items.attrsLabel) {
                items.attrsLabel.map(item => {
                  this.historyColumns.push({
                    header: item,
                    name: item,
                    align: 'center',
                    width: item.length < 30 ? 200 : 250,
                    resizable: true,
                    ellipsis: true
                  });
                });
              }
              items.commonEntityVOs.map(item => {
                Object.keys(item).map(key => {
                  if (item[key].constructor === Object) {
                    return item[key] = JSON.stringify(item[key]);
                  }
                  if (Array.isArray(item[key])) {
                    return item[key] = JSON.stringify(item[key]);
                  }
                });
              });
              this.historyData = items.commonEntityVOs;
              this.$refs.tuiGrid2.invoke('setColumns', this.historyColumns);
              this.$refs.tuiGrid2.invoke('resetData', this.historyData);
            });
      },
      getDataModels() {
        this.$http.get(`/datamodels/attrstree?id=${ this.selected }`)
            .then(response => {
              const status = response.status;
              if (status === 204) {
                return null;
              }
              this.treeData = response.data;
            });
      }
    },
    mounted() {
      this.tuiGrid = this.$refs.tuiGrid1;
      this.getDataModelList();
    }
  }
</script>

<style scoped>
  .card-title {
    font-size: 16px;
    font-weight: bold;
  }
  .btn {
    line-height: 0.5;
  }
  .custom-select {
    font-size: 12px;
    height: 32px;
  }
  .form-control {
    font-size: 12px;
    height: 32px;
  }
  .btn {
    font-size: 12px;
    height: 32px;
  }
  select {
    border: 1px solid #E3E3E3;
    color: #9A9A9A;
  }
  button {
    font-weight: bold;
  }
</style>
