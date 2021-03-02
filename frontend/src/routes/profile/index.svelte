<script>
import Page from "../../components/Page.svelte";
import {
  Container,
  Button,
  TextField,
  Checkbox,
  Icon,
  Card,
  Overlay,
  Alert,
} from "svelte-materialify/src";
import {
  mdiAt,
  mdiEye,
  mdiEyeOff,
  mdiLoading,
  mdiLogin,
  mdiFormTextboxPassword,
  mdiInformationVariant,
  mdiLogout,
  mdiAccountEdit,
} from "@mdi/js";
import { user } from "../_store.js";
import { goto } from "@sapper/app";
import { onMount } from "svelte";
import Chart from "chart.js";

import axios from "axios";

//subscribe user obj
let name;
user.subscribe((value) => {
  if (value) name = value.name;
});

let results;

onMount(async () => {
  getLastResults();
});

async function getLastResults() {
  axios("APP.API/results/search", {
    method: "post",
    // url: "APP.API/results", //?limit=1&offset=0
    headers: {
      "X-Requested-With": "XMLHttpRequest",
      "Content-Type": "application/json",
    },
    params: {
      // sort: sortItems.find((item) => item.value === sort).sort,
      limit: 10,
      offset: 0,
    },
    data: {
      result: {
        user: { id: $user.id },
        // test: {id: 1},
        // classNumber: classNumber == 0 ? null : classNumber,
      },
    },
    withCredentials: true,
  })
    .then((res) => {
      results = res.data.hits;
      console.log(results);
      let data = [];
      results.forEach((result, i) => {
        data.push(
          (result.score / result.maxScore) * 100 == 0
            ? 0.5
            : (result.score / result.maxScore) * 100
        );
      });
      // results = results.sort(function (a, b) {
      //   if (sort == "От старых к новым") return a.id - b.id;
      //   return b.id - a.id;
      // });
      console.log(data);
      renderChart(data);
    })
    .catch((error) => {
      console.log(error);
      if (error.response) {
        // requestErrorMessage = JSON.stringify(error.response.data.errors);
        // requestError = !requestError;
      } else {
        // requestErrorMessage = error.message;
        // requestError = !requestError;
      }
    })
    .finally(() => {
      // loading = false;
    });
}

function renderChart(data) {
  var ctx = document.getElementById("myChart").getContext("2d");
  var chart = new Chart(ctx, {
    type: "bar",
    data: {
      labels: ["10", "9", "8", "7", "6", "5", "4", "3", "2", "1"],
      datasets: [
        {
          label: "Процент баллов за тест",
          backgroundColor: [
            "#1AA2C6",
            "#22B087",
            "#57A336",
            "#D4BB22",
            "#F8B621",
            "#F89216",
            "#E03426",
            "#F64871",
            "#CE69BE",
            "#4E7CBA",
          ],
          borderColor: "rgb(23,66,185)",
          data: data,
        },
      ],
    },
    options: {
      responsive: true,
      legend: {
        position: "bottom",
      },
      hover: {
        mode: "label",
      },
      scales: {
        xAxes: [
          {
            display: true,
            scaleLabel: {
              display: true,
              labelString: "Номер результата",
            },
          },
        ],
        yAxes: [
          {
            display: true,
            ticks: {
              beginAtZero: true,
              steps: 10,
              stepValue: 5,
              max: 100,
            },
          },
        ],
      },
    },
  });

  let dataset = chart.data.datasets[0];
  dataset.data.forEach((value, i) => {
    if (value >= 87) {
      dataset.backgroundColor[i] = "#57A336";
    } else if (value >= 66) {
      dataset.backgroundColor[i] = "#F8B621";
    } else if (value >= 42) {
      dataset.backgroundColor[i] = "#F89216";
    } else if (value >= 0) {
      dataset.backgroundColor[i] = "#E03426";
    }
  });

  // for (var i = 0; i < dataset.data.length; i++) {
  //   if (dataset.data[i] > colorChangeValue) {
  //     dataset.backgroundColor[i] = "#ff0000";
  //   }
  // }

  chart.update();
}
</script>

<svelte:head>
  <title>Профиль</title>
</svelte:head>

<Page authenticationRequired>
  <Container class="text-center" style="max-width:1200px;">
    <Card class="pa-4" style="background: #fefefe">
      <h4>Профиль</h4>

      <hr
        class="mt-1 mb-8"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

      <div class="d-none d-md-flex justify-space-around">
        <p
          class="text-center"
          style="line-height: 36px; display: table-cell; vertical-align: middle;">
          Добро пожаловать,
          {name}!
        </p>
        <p>
          <Button
            class="primary-color"
            on:click={() => {
              goto("/profile/edit");
            }}>
            <Icon path={mdiAccountEdit} />
            Редактировать данные профиля
          </Button>
          <Button
            class="secondary-color"
            on:click={() => {
              $user = undefined;
              goto("/");
            }}>
            <Icon path={mdiLogout} />
            Выйти
          </Button>
        </p>
      </div>

      <div class="d-md-none">
        <p class="text-center">Добро пожаловать, {name}!</p>
        <p>
          <Button
            class="primary-color"
            on:click={() => {
              goto("/profile/edit");
            }}>
            <Icon path={mdiAccountEdit} />
            Редактировать данные профиля
          </Button>
        </p>
        <p>
          <Button
            class="secondary-color"
            on:click={() => {
              $user = undefined;
              goto("/");
            }}>
            <Icon path={mdiLogout} />
            Выйти
          </Button>
        </p>
      </div>

      <hr
        class="mt-4 mb-4"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

      <h5>Статистика</h5>

      <h6>10 последних результатов</h6>

      <canvas id="myChart" />
    </Card>
  </Container>
</Page>
