<template>
  <div class="content" ref="content">
    <!--        <div>-->
    <!--          <div class="layoutJSON">-->
    <!--            Displayed as <code>[x, y, w, h]</code>:-->
    <!--            <div class="columns">-->
    <!--              <div class="layoutItem" v-for="item in layout">-->
    <!--                <b>{{ item.i }}</b>: [{{ item.x }}, {{ item.y }}, {{ item.w }}, {{ item.h }}]-->
    <!--              </div>-->
    <!--            </div>-->
    <!--          </div>-->
    <!--        </div>-->
    <div class="container-fluid">
      <div class="header text-right">
        <div class="accordion" @click="accordion = !accordion">
          <i class="nc-icon nc-stre-up" v-if="accordion"></i>
          <i class="nc-icon nc-stre-down" v-else-if="!accordion"></i>
        </div>
      </div>
      <div class="row pt-2" v-show="accordion">
        <div class="col-md-5 col-sm-12">
          <p class="ml-2" style="font-size: 12px;">※ {{ $t('message.dashboardDescription') }}</p>
        </div>
        <div class="col-md-3 col-sm-12">
          <el-select
            class="mr-sm-2"
            v-model="selectedDashboard"
            placeholder="Select"
            size="small"
            style="width: 100%;"
            @change="onDashboardChange"
            value-key="dashboardId"
          >
            <el-option
              v-for="item in dashboardList"
              :key="item.dashboardId"
              :label="item.dashboardName"
              :value="item"
            >
            </el-option>
          </el-select>
        </div>
        <div class="col-md-4 col-sm-12 el-col-xs-24 text-right">
          <div class="row flex-row-reverse">
            <el-popover
              placement="top"
              width="200"
              v-model="messageVisible">
              <p style="font-size: 12px;">{{ $t('message.deleteCheck') }}</p>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="" @click="messageVisible = false">{{ $t('comm.cancel') }}</el-button>
                <el-button type="primary" size="mini" @click="deleteDashboard">{{ $t('comm.ok') }}</el-button>
              </div>
              <el-button
                slot="reference"
                class="mr-0"
                size="small"
                type="info"
              >
                {{ $t('dashboard.deleteDashboard') }}
              </el-button>
            </el-popover>
            <el-button
              class="mr-2"
              size="small" type="info"
              @click="updateDashboard"
            >
              {{ $t('dashboard.saveDashboard') }}
            </el-button>
            <el-button
              class="mr-2"
              size="small" type="info"
              @click="addDashboard"
            >
              {{ $t('dashboard.addDashboard') }}
            </el-button>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-9 col-sm-12">
          <div class="dashboard-title">
            <h3 v-if="!isTitleEdited" @dblclick="activeTitleEdit">{{ selectedDashboard.dashboardName }}</h3>
            <input v-else type="text" v-model="selectedDashboard.dashboardName" class="form-control "
                   @blur="inactiveTitleEdit"/>
          </div>
        </div>
        <div class="col-md-3 col-sm-12">
          <div class="row flex-row-reverse mt-2">
            <el-button type="info" @click="layoutSave" size="small">{{ $t('dashboard.saveLayout') }}</el-button>
            <el-button class="mr-2" size="small" type="primary" @click="showChartPopup"> +
              {{ $t('dashboard.addWidget') }}
            </el-button>
          </div>
        </div>

      </div>
      <GridLayout ref="gridlayout"
                  :layout.sync="layout"
                  :col-num="12"
                  :row-height="30"
                  :is-draggable="true"
                  :is-resizable="true"
                  :is-mirrored="false"
                  :vertical-compact="true"
                  :margin="[10, 10]"
                  :use-css-transforms="true"
      >
        <GridItem v-for="item in layout"
                  :key="item.widgetId"
                  :x="item.x"
                  :y="item.y"
                  :w="item.w"
                  :h="item.h"
                  :i="item.i"
                  drag-allow-from=".vue-draggable-handle"
                  drag-ignore-from=".no-drag"
                  @resize="(i, newH, newW, newHPx, newWPx) => resizeEvent(i, newH, newW, newHPx, newWPx, item)"
                  @resized="(i, newH, newW, newHPx, newWPx) => resizedEvent(i, newH, newW, newHPx, newWPx, item)"
                  @container-resized="(i, newH, newW, newHPx, newWPx) => containerResizedEvent(i, newH, newW, newHPx, newWPx, item)"
                  :style="{zIndex: item.chartType !== 'map_latest' ? 999 : 998}"
        >
          <div class="chartWrapper">
            <div class="vue-draggable-handle" @dblclick="onChartEdit(item)"></div>
            <div class="no-drag">
              <CardChart class="chartWrapper" v-if="isCardChart(item)" :type="item.chartType" :options="item"
                         :data="item.data"/>
              <LineChart class="chartWrapper"
                         v-if="item.chartType === 'line'"
                         :options="item.options"
                         :chartData="item.data"
              />
              <Doughnut class="chartWrapper" v-if="item.chartType === 'donut'" :options="item.options"
                        :chartData="item.data"/>
              <BarChart class="chartWrapper"
                        v-if="item.chartType === 'bar'"
                        :options="item.options"
                        :chartData="item.data"
              />
              <PieChart class="chartWrapper" v-if="item.chartType === 'pie'" :options="item.options"
                        :chartData="item.data"/>
              <LatestMapChart
                class="chartWrapper"
                v-if="item.chartType === 'map_latest'"
                :mapSearchConditionId="item.mapSearchConditionId"
                :chartTitle="item.title"
                :chartKey="`latest_map_${item.i}`"
              />
              <HistogramChart class="chartWrapper" v-if="item.chartType === 'histogram'"
                        :options="item.options" :chartData="item.data"/>
            </div>
          </div>
        </GridItem>
      </GridLayout>
    </div>
    <AddWidgetPopup v-if="dialogVisible" :editItem="editItem"
                    @close="onCloseAddWidget" @add="onAddWidget" @edit="onEditWidget" @remove="onRemoveWidget"
                    :layout.sync="layout" :dashboardId="selectedDashboard.dashboardId"/>
  </div>
</template>

<script>
/**
 * Dashboard Page
 * Manage dashboards and display widgets.
 *
 * @component
 * - AddWidgetPopup,
 * - GridLayout,
 * - GridItem,
 * - CardChart,
 * - PieChart,
 * - BarChart,
 * - LineChart,
 * - Doughnut,
 * - LatestMapChart
 * - HistogramChart
 * - element-ui
 * @props props { ... }
 * @state data() { ... }
 */
import VueGridLayout from 'vue-grid-layout';
import Doughnut from '@/components/Chart/Doughnut';
import LineChart from '@/components/Chart/LineChart';
import BarChart from '@/components/Chart/BarChart';
import PieChart from '@/components/Chart/PieChart';
import CardChart from '@/components/Chart/CardChart';
import LatestMapChart from '@/components/Chart/LatestMapChart';
import HistogramChart from '@/components/Chart/HistogramChart';
import AddWidgetPopup from "@/pages/Widgets/AddWidgetPopup";

import {dashboardApi, widgetApi} from '@/moudules/apis';
import {
  setDonutChart,
  setBarChartLast,
  setBarChartHistory,
  setLineChart,
  setHistogramChartHistory,
  setHistogramChartLast,
  chartOptions,
  barChartOptions,
  lineChartOptions,
  histogramChartOptions,
} from '@/components/Chart/Dataset';

const GridLayout = VueGridLayout.GridLayout;
const GridItem = VueGridLayout.GridItem;

export default {
  name: 'Dashboard',
  components: {
    AddWidgetPopup,
    GridLayout,
    GridItem,
    CardChart,
    PieChart,
    BarChart,
    LineChart,
    Doughnut,
    LatestMapChart,
    HistogramChart
  },
  data() {
    return {
      accordion: false,
      dialogVisible: false,
      layout: [],
      isBtnShow: false,
      entityIdArr: [],
      treeData: [],
      treeId: null,
      treeRow: null,
      treeNode: null,
      messageVisible: false,
      colNum: 12,
      index: 0,
      editItem: null,
      websocket: null,
      isTitleEdited: false,
      selectedDashboard: {
        dashboardName: null,
        dashboardId: null,
      },
      dashboardList: [],
      scrollInterval: null,
    }
  },
  methods: {
    // If the screen is full
    // scroll so that unlimited size can be adjusted when adjusting the widget size located at the bottom.
    async pageScroll(newHeight) {
      if (!this.scrollInterval) {
        this.scrollInterval = setInterval(() => {
          const element = this.$refs.content;
          if (newHeight > 0) {
            if (element.scrollHeight - element.scrollTop !== element.offsetHeight) {
              // Keep scrolling down when scrollable.
              element.scrollBy(0, 10)
            }
          }
        }, 10);
      }
    },
    // Since height is fixed in the map map, it needs to be changed dynamically.
    resizeEvent(i, newH, newW, newHPx, newWPx, item) {
      this.pageScroll(newH - item.h);
      if (item.chartType === 'map_latest') {
        this.$el.querySelector(`#latest_map_${i} .vue-map`).style.height = `${newHPx - 70}px`;
      }
    },
    resizedEvent(i, newX, newY, newHPx, newWPx, item) {
      // When the widget resizing event ends, scroll ends.
      clearInterval(this.scrollInterval);
      this.scrollInterval = null;
      if (item.chartType === 'map_latest') {
        this.$el.querySelector(`#latest_map_${i} .vue-map`).style.height = `${newHPx - 70}px`;
      }
    },
    containerResizedEvent(i, newH, newW, newHPx, newWPx, item) {
      if (item.chartType === 'map_latest') {
        this.$el.querySelector(`#latest_map_${i} .vue-map`).style.height = `${newHPx - 70}px`;
      }
    },
    // websocket connection
    socketConnect() {
      if (!this.websocket) {
        // TODO process.env - subtract it as an environmental variable.
        // const serverURL = 'ws://localhost:8084/widgetevents'
        const serverURL = `ws://${window.location.host}/widgetevents`;
        this.websocket = new WebSocket(serverURL);

        this.websocket.onopen = (event) => {
          console.log('websocket open');
          console.log(event);
          this.onOpen();
        };

        this.websocket.onmessage = (event) => {
          console.log('websocket onMessage');
          console.log(event);
          this.onMessage(event);
        };

        this.websocket.onclose = (event) => {
          console.log('websocket close');
          console.log(event);
        };
      }
    },
    sendMessage(message) {
      message.dashboardId = this.selectedDashboard.dashboardId;
      this.websocket.send(JSON.stringify(message));
    },
    onOpen() {
    },
    async onMessage(event) {
      const socketData = JSON.parse(event.data);
      console.log(socketData);
      if (socketData.chartType === 'text' || socketData.chartType === 'boolean' || socketData.chartType === 'custom_text') {
        const index = this.layout.findIndex(item => item.widgetId === socketData.widgetId);
        this.layout[index].data = {result: JSON.parse(event.data)};
        return null;
      }

      let resultData = null;
      if (socketData.chartType === 'donut' || socketData.chartType === 'pie') {
        resultData = setDonutChart(socketData);
      }

      if (socketData.chartType === 'bar') {
        if (socketData.dataType === 'last') {
          resultData = setBarChartLast(socketData);
        } else {
          resultData = setBarChartHistory(socketData);
        }
      }

      if (socketData.chartType === 'line') {
        resultData = setLineChart(socketData);
      }

      if (socketData.chartType === 'histogram') {
        if (socketData.dataType === 'last') {
          resultData = setHistogramChartLast(socketData);
        } else {
          resultData = setHistogramChartHistory(socketData);
        }
      }

      this.layout.some(item => {
        if (item.widgetId === socketData.widgetId) {
          item.data = resultData;
          if (item.chartType === 'bar') {
            item.options = barChartOptions(item);
          } else if (item.chartType === 'line') {
            item.options = lineChartOptions(item);
          } else if (item.chartType === 'histogram') {
            item.options = histogramChartOptions(item, socketData.data.length);
          } else {
            item.options = chartOptions(item);
          }
        }
      });
    },
    disconnect() {
      if (this.websocket) {
        this.websocket.close();
        this.websocket = null;
      }
    },
    async reconnect() {
      if (!this.websocket) {
        this.socketConnect();
      } else {
        await this.disconnect();
        await this.socketConnect();
      }
    },
    onChartEdit(item) {
      this.editItem = item;
      this.dialogVisible = true;
    },
    mouseout() {
      this.isBtnShow = false;
    },
    showChartPopup() {
      this.dialogVisible = true;
    },
    layoutSave() {
      const {dashboardId} = this.selectedDashboard;
      // make data
      const chartSize = this.layout.map(item => {
        const {widgetId, i, x, y, w, h} = item;
        return {
          dashboardId,
          widgetId,
          chartSize: JSON.stringify({
            i, x, y, w, h
          })
        }
      });
      widgetApi.update(chartSize)
        .then(data => {
          this.$alert(this.$i18n.t('message.saved'));
        });
    },
    activeTitleEdit() {
      this.isTitleEdited = true;
    },
    inactiveTitleEdit(event) {
      this.isTitleEdited = false;
    },
    async onDashboardChange(selected) {
      this.layout = [];
      this.messageVisible = false
      await this.reconnect();
      await this.getWidgetList();
    },
    onCloseAddWidget() {
      this.dialogVisible = false;
      this.editItem = null;
    },
    onEditWidget(data) {
      const {chartType, userId, widgetId, i} = data;

      if (chartType === 'Image') {
        this.getWidgetImage(i, widgetId);
        this.layout[i].widgetId = widgetId;
      } else {
        const index = this.layout.findIndex(item => item.widgetId === widgetId);
        Object.keys(data).forEach(key => {
          this.layout[index][key] = data[key];
        })
      }

      if (chartType !== 'custom_text' && chartType !== 'Image') {
        // call websocket
        this.sendMessage({widgetId, userId, methods: 'update'});
      }
    },
    onRemoveWidget(data) {
      const {chartType, widgetId, userId} = data;
      this.layout = this.layout.filter(item => item.widgetId !== widgetId);

      if (chartType !== 'custom_text' && chartType !== 'Image') {
        // call websocket
        this.sendMessage({widgetId, userId, method: 'delete'});
      }
      // Increment the counter to ensure key is always unique.
      this.index--;
    },
    onAddWidget(data) {
      const {chartType, widgetId, userId} = data;
      this.layout.push(data);

      if (chartType === 'Image') {
        this.getWidgetImage(this.index, widgetId);
      }

      if (chartType !== 'custom_text' && chartType !== 'Image') {
        // call websocket
        this.sendMessage({widgetId, userId, methods: 'create'});
      }
      // Increment the counter to ensure key is always unique.
      this.index++;
    },
    getDashboard() {
      dashboardApi.fetch()
        .then(data => {
          this.dashboardList = data || [];
          if (data.length > 0) {
            this.selectedDashboard = this.dashboardList[0];
            this.getWidgetList();
          } else this.initSelectedDashboard();
        })
    },
    addDashboard() {
      const dashboard = {
        dashboardName: this.$i18n.t('message.defaultWidgetTitle')
      }
      dashboardApi.create(dashboard)
        .then(data => {
          this.dashboardList.push(data);
          this.selectedDashboard = data;
          this.layout = [];
        })
        .then(() => this.getWidgetList());
    },
    deleteDashboard() {
      const {dashboardId} = this.selectedDashboard;
      dashboardApi.delete(dashboardId)
        .then(() => {
          this.$alert(this.$i18n.t('message.deleted'));
          this.dashboardList = this.dashboardList.filter(item => item.dashboardId !== dashboardId);
          if (this.dashboardList.length > 0) {
            this.selectedDashboard = this.dashboardList[0];
            this.getWidgetList();
          } else {
            this.initSelectedDashboard();
            this.layout = [];
          }
        })
        .finally(() => {
          this.messageVisible = false;
        });
    },
    updateDashboard() {
      dashboardApi.modify(this.selectedDashboard)
        .then(() => {
          this.$alert(this.$i18n.t('message.saved'));
        })
    },
    // widget
    getWidgetList() {
      this.layout = [];
      this.index = 0;
      const {dashboardId} = this.selectedDashboard;
      widgetApi.fetch(dashboardId)
        .then(data => {
          const items = data;
          if (items.length > 0) {
            items.forEach(item => {
              const {
                widgetId,
                userId,
                chartType,
                chartTitle,
                chartXName,
                chartYName,
                yaxisRange,
                mapSearchConditionId
              } = item;
              const chartSize = JSON.parse(item.chartSize);
              this.layout.push({
                widgetId: widgetId,
                x: chartSize.x,
                y: chartSize.y,
                w: chartSize.w,
                h: chartSize.h,
                i: this.index,
                chartType: chartType,
                title: chartTitle,
                chartXName: chartXName || null,
                chartYName: chartYName || null,
                yaxisRange: yaxisRange || null,
                data: null,
                mapSearchConditionId: mapSearchConditionId
              });

              if (chartType === 'custom_text') {
                const {extention1, extention2} = item;
                this.layout[this.index].data = {
                  extention1,
                  extention2,
                }
              } else if (chartType === 'Image') {
                this.getBase64Image(this.index, item.file, item.extention1);
              } else {
                // send websocket message
                this.sendMessage({widgetId, userId, methods: 'read'});
              }
              this.index++;
            });
          }
        })
      .finally(() => {

        // TODO 더미데이터 셋팅
        /*
        const socketData = {
          // entityIds: ["AAA", "BBB", "CCC"],
          entityIds: ["AAA"],
          data: [
            // TODO 예제로 주신건 chartjs 3버전이라서 2 버전으로 하고자 하면 chartValue 단일 리스트 필요
            // x가 나누고자 하던 항목 단위에서, 종료 값? 으로 변경 필요
            // https://www.educative.io/edpresso/chartjs---create-a-histogram
            // https://stackoverflow.com/questions/51880101/make-a-histogram-in-chart-js
            {id: 'AAA', chartValue: {x: 1, y: 100}},
            {id: 'AAA', chartValue: {x: 2, y: 100}},
            {id: 'AAA', chartValue: {x: 3, y: 300}},
            {id: 'AAA', chartValue: {x: 4, y: 200}},
            {id: 'AAA', chartValue: {x: 5, y: 200}},
            {id: 'AAA', chartValue: {x: 6, y: 200}},
            {id: 'AAA', chartValue: {x: 7, y: 200}},
            {id: 'AAA', chartValue: {x: 8, y: 200}},
            {id: 'AAA', chartValue: {x: 9, y: 200}},
            {id: 'AAA', chartValue: {x: 10, y: 200}},
            {id: 'AAA', chartValue: {x: 11, y: 200}},
            {id: 'AAA', chartValue: {x: 12, y: 200}},
            {id: 'AAA', chartValue: {x: 13, y: 200}},
            {id: 'AAA', chartValue: {x: 14, y: 200}},
            {id: 'AAA', chartValue: {x: 15, y: 200}},
            {id: 'AAA', chartValue: {x: 16, y: 200}},
            // {id: 'BBB', chartValue: 300},
            // {id: 'CCC', chartValue: 200},
          ]
        }
         */
        const socketData = {
          "totalCount": 100,
          "widgetId": "89c21dad-09ca-481c-910b-6a2b13975e62",
          "chartType": "histogram",
          "dataType": "history",
          "attributeId": "availableSpotNumber",
          "data": [
            {
              "x": 5,
              "y": 0
            },
            {
              "x": 15,
              "y": 0
            },
            {
              "x": 25,
              "y": 4
            },
            {
              "x": 35,
              "y": 5
            },
            {
              "x": 45,
              "y": 5
            },
            {
              "x": 55,
              "y": 7
            },
            {
              "x": 65,
              "y": 6
            },
            {
              "x": 75,
              "y": 8
            },
            {
              "x": 85,
              "y": 9
            },
            {
              "x": 95,
              "y": 2
            }
          ],
          "entityIds": [
            "urn:datahub:OffStreetParking:yt_lot_2"
          ]
        }
        // const chartUnit = this.formData.extention1; // TODO 실제 해당 값으로 설정 필요
        const chartUnit = 10; // 데미데이터
        const resultData = setHistogramChartHistory(socketData, chartUnit);
        this.layout.forEach(item => {
          if (item.chartType === 'histogram') {
            item.data = resultData;
            item.options = histogramChartOptions(item, chartUnit, socketData.totalCount);
          }
        });
      });
    },
    getBase64Image(index, file, name) {
      const url = `data:image/png;base64,${file}`
      this.layout[index].data = {url, name};
    },
    async getWidgetImage(index, widgetId) {
      const {dashboardId} = this.selectedDashboard;
      const file = await widgetApi.fetchImage(dashboardId, widgetId);
      const url = widgetApi.fetchImageUrl(dashboardId, widgetId);
      this.layout[index].data = {url, file};
    },
    isCardChart(item) {
      switch (item.chartType) {
        case 'text':
        case 'boolean':
        case 'custom_text':
        case 'Image':
          return true;
      }
      return false;
    },
    initSelectedDashboard() {
      this.selectedDashboard = {
        dashboardName: null,
        dashboardId: null,
      }
    },
  },
  mounted() {
    // Reconnect the page in case it does not refresh or close.
    this.reconnect();

    this.index = this.layout.length > 0 ? this.layout.length - 1 : 0;
    this.getDashboard();
  },
}
</script>

<style scoped>
.header .accordion {
  width: 30px;
  height: 30px;
  margin-left: auto;
  margin-right: 0;
  border-radius: 50%;
  text-align: center;
  padding-top: 6px;
}
.header .accordion:hover {
  cursor: pointer;
  background: rgba(2, 2, 2, 0.05);
}

.main-panel > .content {
  background-color: #f7f7f8 !important;
  overflow-x: hidden;
  height: calc(100% - 60px);
  padding-top: 5px !important;
}

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

.control-label {
  font-size: 12px;
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

label {
  margin-bottom: 0;
}

.circle {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: #dddddd;
  text-align: center;
}

.circle:hover {
  background: #e4e3e3;
}

.chartWrapper {
  position: relative;
  height: 100%;
  width: 100%;
}

.chartLabel {
  position: relative;
  width: 100%;
  text-align: center;
}

.droppable-element {
  width: 150px;
  text-align: center;
  background: #fdd;
  border: 1px solid black;
  margin: 10px 0;
  padding: 10px;
}

.vue-grid-layout {
  background: rgb(247 247 248);
}

.vue-grid-item {
  background: #fff;
  border-radius: 4px;
  border: 1px solid #eee;
}

.layoutJSON {
  background: #ddd;
  border: 1px solid black;
  border-radius: 4px;
  padding: 10px;
  margin: 10px;
}

.vue-grid-item .no-drag {
  height: 100%;
  width: 100%;
}

.vue-grid-item .add {
  cursor: pointer;
}

.vue-draggable-handle {
  position: absolute;
  width: 25px;
  height: 10px;
  top: 5px;
  right: 5px;
  padding: 0 8px 8px 0;
  background-origin: content-box;
  background-color: black;
  box-sizing: border-box;
  border-radius: 5px;
  cursor: move;
  z-index: 999;
  opacity: 0.2;
}

.dashboard-title {
  height: 30px;
}

.dashboard-title * {
  font-weight: bold;
  font-size: 1.4em;
}

</style>
