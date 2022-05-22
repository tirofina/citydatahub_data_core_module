import { CHART_COLORS, namedColor, color, transparentize } from '@/components/Chart/Utils';
import moment from 'moment';


/**
 * Dataset by type of dashboard widget
 */

// Chart default options
export const chartOptions = (option) => {
  return {
    responsive: true,
    maintainAspectRatio: false,
    title: {
      display: true,
      text: option.title
    }
  };
};

// Bar Chart options
export const barChartOptions = (option) => {
  let ticks = {};
  if (option.yaxisRange) {
    ticks = {
      beginAtZero: true,
      min: parseInt(option.yaxisRange.split('-')[0]),
      max: parseInt(option.yaxisRange.split('-')[1]),
    };
  } else {
    ticks = {
      beginAtZero: true
    };
  }
  return {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      yAxes: [{
        ticks: ticks,
        scaleLabel: {
          display: !!option.chartYName,
          labelString: option.chartYName
        }
      }],
      xAxes: [{
        scaleLabel: {
          display: !!option.chartXName,
          labelString: option.chartXName
        }
      }],
    },
    title: {
      display: true,
      text: option.title
    }
  };
};

// Line Chart options
export const lineChartOptions = (option) => {
  let ticks = {};
  if (option.yaxisRange) {
    ticks = {
      beginAtZero: true,
      min: parseInt(option.yaxisRange.split('-')[0]),
      max: parseInt(option.yaxisRange.split('-')[1]),
    };
  } else {
    ticks = {
      beginAtZero: true
    };
  }
  return {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      yAxes: [{
        ticks: ticks,
        scaleLabel: {
          display: !!option.chartYName,
          labelString: option.chartYName
        }
      }],
      xAxes: [{
        scaleLabel: {
          display: !!option.chartXName,
          labelString: option.chartXName,
        }
      }],
    },
    title: {
      display: true,
      text: option.title
    }
  };
};

// Donut, Pie Chart dateset data binding
export const setDonutChart = (donutData) => {
  const rendomIndex = Math.floor(Math.random() * 13);
  let labels = [];
  let datasets = [];
  let data = [];
  let bgColor = [];

  // Setting the data according to the received array value.
  donutData.data.map((item, index) => {
    labels.push(item.id);
    data.push(item.chartValue);
    bgColor.push(color(rendomIndex + index));
  });
  datasets.push({
    label: 'donut',
    data: data,
    backgroundColor: bgColor,
    hoverOffset: 4
  });
  return { labels: labels, datasets: datasets };
};

// Bar Chart dateset data binding
export const setBarChartLast = (barData) => {
  const rendomIndex = Math.floor(Math.random() * 13);
  let labels = [];
  let datasets = [];
  let data = [];
  let bgColor = [];
  let borderColor = [];

  // Setting the data according to the received array value.
  barData.data.map((item, index) => {
    labels.push(item.id);
    data.push(item.chartValue);
    bgColor.push(transparentize(color(rendomIndex + index), 0.5));
    borderColor.push(color(rendomIndex + index));
  });
  datasets.push({
    label: barData.attributeId,
    data: data,
    borderColor: borderColor,
    backgroundColor: bgColor,
    borderWidth: 1
  });
  return { labels: labels, datasets: datasets };
};

// Bar History Chart dateset data binding
export const setBarChartHistory = (barData) => {
  const rendomIndex = Math.floor(Math.random() * 13);
  let labels = [];
  let datasets = [];

  //If you have two entity IDs, you need to create two data sets.
  // barchart single id
  let chartLabels = barData.entityIds;
  chartLabels.forEach(entityId =>
      datasets.push({
        label: entityId,
        data: [],
        borderColor: color(rendomIndex),
        backgroundColor: transparentize(color(rendomIndex), 0.5),
        borderWidth: 1
      }));

  // Find and map the entity ID corresponding to the created data set.
  // Data extract
  barData.data.forEach((item, index) => {
    datasets.map(d => item.id === d.label && d.data.push(item.chartValue));
    labels.push(moment(item.observedAt).format('YYYY-MM-DD HH:mm:ss'));
  });
  return { labels: labels, datasets: datasets };
};

/**
 * Data (datasets, label) setting for line widget type
 *
 * @param lineData
 * @returns {{datasets: *[], labels: *[]}}
 */
export const setLineChart = (lineData) => {
  const rendomIndex = Math.floor(Math.random() * 13);
  let labels = [];
  let datasets = [];

  // If you have two entity IDs, you need to create two data sets.
  let chartLabels = lineData.entityIds;
  chartLabels.forEach((entityId, index) =>
      datasets.push({ label: entityId, data: [], fill: false, borderColor: color(rendomIndex + index), tension: 0.1 }));

  // Find and map the entity ID corresponding to the created data set.
  // Data extract
  lineData.data.forEach((item, index) => {
    datasets.map(d => item.id === d.label && d.data.push(item.chartValue));
    labels.push(moment(item.observedAt).format('YYYY-MM-DD HH:mm:ss'));
  });

  return { labels: labels, datasets: datasets };
};

// Histogram History Chart dateset data binding
export const setHistogramChartHistory = (barData) => {
  const randomIndex = Math.floor(Math.random() * 13);
  let labels = [];
  let datasets = [];

  //If you have two entity IDs, you need to create two data sets.
  // barchart single id
  let chartLabels = barData.entityIds;
  chartLabels.forEach(entityId =>
    datasets.push({
      label: entityId,
      data: [],
      borderColor: color(randomIndex),
      backgroundColor: transparentize(color(randomIndex), 0.5),
      borderWidth: 1
    }));

  // Find and map the entity ID corresponding to the created data set.
  // Data extract
  // TODO 데이터 받아서 확인 필요
  barData.data.forEach((item, index) => {
    datasets.map(d => item.id === d.label && d.data.push(item.chartValue));
    labels.push(moment(item.observedAt).format('YYYY-MM-DD HH:mm:ss'));
  });
  return { labels: labels, datasets: datasets };
};
