<script context="module">
export async function preload({ params }) {
  return { params: params };
}
</script>

<script>
import Page from "../../components/Page.svelte";
import {
  Container,
  Card,
  CardTitle,
  CardSubtitle,
  CardActions,
  Button,
  Icon,
  Divider,
  List,
  ListItem,
  Slider,
  Select,
  TextField,
} from "svelte-materialify/src";
import ExpansionPanels, {
  ExpansionPanel,
} from "svelte-materialify/src/components/ExpansionPanels";
import {
  mdiSchoolOutline,
  mdiClockOutline,
  mdiFlagCheckered,
  mdiPlus,
  mdiMinus,
  mdiRefresh,
  mdiChevronDown,
  mdiEye,
  mdiFileSearchOutline,
} from "@mdi/js";
import ContentLoader from "svelte-content-loader";
import { user } from "../_store.js";
import { goto } from "@sapper/app";
import { fade, slide } from "svelte/transition";
import { onMount } from "svelte";
import axios from "axios";
import formatDistanceToNow from "date-fns/formatDistanceToNow";
import format from "date-fns/format";
import { ru } from "date-fns/locale";

let subjects = {
  mathematics: "Математика",
  geometry: "Геометрия",
  physics: "Физика",
  russian: "Русский язык",
  informatics: "Информатика",
  history: "История",
  english: "Английский язык",
  literature: "Литература",
  chemistry: "Химия",
  biology: "Биология",
  geography: "География",
  social_studies: "Обществознание",
};

let searchOptionsActive = false;

let title = "";
let classNumber = 0;
let sort = "От новых к старым";

let sortItems = [
  { name: "От новых к старым", value: "От новых к старым", sort: "DESC" },
  { name: "От старых к новым", value: "От старых к новым", sort: "ASC" },
];

let openedTests = [];

let loading = true;
let tests = [];

onMount(async () => {
  searchTests();
});

async function searchTests() {
  axios("APP.API/tests/search", {
    method: "post",
    url: "APP.API/tests", //?limit=1&offset=0
    headers: {
      "X-Requested-With": "XMLHttpRequest",
      "Content-Type": "application/json",
    },
    params: {
      // sort: sortItems.find((item) => item.value === sort).sort,
    },
    data: {
      test: {
        title: title == "" ? null : title,
        subject: params.subject,
        classNumber: classNumber == 0 ? null : classNumber,
      },
    },
    withCredentials: true,
  })
    .then((res) => {
      tests = res.data.hits;
      tests = tests.sort(function (a, b) {
        if (sort == "От старых к новым") return a.id - b.id;
        return b.id - a.id;
      });
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
      loading = false;
    });
}

const titleRules = [
  // (v) => (v.length >= 0 && length <= 255) || "Введите минимум 3 символа",
  (v) => v.length <= 255 || "Максимум 255 символов",
];

function onChange() {
  if (title.length >= 0 && title.length <= 255) searchTests();
}

export let params;
</script>

<svelte:head>
  <title>{params.subjects}</title>
</svelte:head>

<Page>
  <Container style="max-width:1000px;">
    <Card class="card pa-4">
      <h4 class="text-center">{subjects[params.subject]}</h4>
      <hr
        class="mt-1 mb-4"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

      <div class="d-flex justify-center mt-4 mb-4">
        <Card style="max-width:900px; width: 100%;">
          <CardTitle class="primary-color white-text">
            <h5>Параметры поиска</h5>
            <Button
              text
              fab
              size="small"
              class="ml-auto"
              on:click={() => {
                searchOptionsActive = !searchOptionsActive;
              }}>
              <Icon
                path={mdiChevronDown}
                rotate={searchOptionsActive ? 180 : 0} />
            </Button>
            <!-- <CardSubtitle>1,000 miles of wonder</CardSubtitle> -->
            <!-- <CardActions> -->
          </CardTitle>
          <!-- </CardActions> -->
          {#if searchOptionsActive}
            <Divider />
            <div transition:slide class="pl-4 pr-4 pt-2 pb-2">
              <TextField
                bind:value={title}
                clearable
                outlined
                counter="255"
                maxlength="255"
                rules={titleRules}
                class="mt-2"
                on:input={onChange}>
                <div slot="prepend">
                  <Icon path={mdiFileSearchOutline} />
                </div>
                Поиск теста
              </TextField>

              <p class="text-center" style="height: 35px;">
                <b>Класс: {classNumber == 0 ? "Все (1-11)" : classNumber} </b>
              </p>
              <div class="slider">
                <Slider
                  thumb
                  persistentThumb
                  min={0}
                  max={11}
                  step={1}
                  bind:value={classNumber}
                  inverseLabel
                  color="primary">
                  <span slot="prepend-outer">
                    <Button
                      icon
                      class="primary-text slider-button"
                      on:click={() => {
                        classNumber--;
                      }}>
                      <Icon path={mdiMinus} class="default-icon" />
                    </Button>
                  </span>
                  <span slot="append-outer" class="ml-0">
                    <Button
                      icon
                      class="primary-text slider-button"
                      on:click={() => {
                        classNumber++;
                      }}>
                      <Icon path={mdiPlus} class="default-icon" />
                    </Button>
                  </span>
                </Slider>
              </div>
              <div style="max-width: 250px; margin: 0 auto;">
                <Select outlined items={sortItems} bind:value={sort}>
                  Сортировка
                </Select>
              </div>
              <div class="d-flex justify-center mt-3" style="width: 100%">
                <Button
                  class="primary-color"
                  on:click={() => {
                    searchTests();
                  }}
                  rounded
                  size="large">
                  <Icon path={mdiRefresh} />
                  Обновить
                </Button>
              </div>
            </div>
          {/if}
        </Card>
      </div>

      <!-- <hr
        class="mt-1 mb-8"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" /> -->

      <div class="d-flex justify-center">
        {#if tests.length > 0}
          <ExpansionPanels
            multiple
            popout
            bind:value={openedTests}
            style="max-width: 900px; width: 100%;">
            <!-- <List class="elevation-2" style="max-width: 900px; width: 100%;"> -->
            {#each tests || [] as test}
              <div style="width: 100%;">
                <ExpansionPanel>
                  <span slot="header">
                    <h6>{test.title}</h6>
                    <Icon path={mdiSchoolOutline} class="default-icon" />
                    {test.classNumber}
                    класс
                    <Icon path={mdiClockOutline} class="default-icon" />
                    Добавлен:
                    {formatDistanceToNow(
                      new Date(test.createdAt.split("[")[0]),
                      {
                        addSuffix: true,
                        locale: ru,
                      }
                    )}</span>
                  <div class="d-block" style="width: 100%;">
                    <div style="min-width=100%;">{test.description}</div>
                    <div class="d-block mt-1 float-right">
                      <Button
                        class="primary-color"
                        on:click={() => {
                          openedTests = [];
                          goto(`/test/view/${test.id}`, { scroll: true });
                        }}
                        rounded
                        size="large">
                        <Icon path={mdiEye} />
                        Посмотреть
                      </Button>
                      <Button
                        class="primary-color"
                        on:click={() => {
                          openedTests = [];
                          goto(`/test/solve/${test.id}`, { scroll: true });
                        }}
                        rounded
                        size="large">
                        <Icon path={mdiFlagCheckered} />
                        Пройти тест
                      </Button>
                    </div>
                  </div>
                </ExpansionPanel>
              </div>
            {/each}
          </ExpansionPanels>
        {:else}Пока здесь ничего нет{/if}
        <!-- </List> -->
      </div>

      {#if loading}
        {#each Array(5) as _}
          <p>
            <ContentLoader
              width={400}
              height={60}
              primaryColor="#f0f0f7"
              secondaryColor="#fafafa"
              uniqueKey>
              <rect x="70" y="15" rx="5" ry="5" width="300" height="15" />
              <rect x="70" y="39" rx="5" ry="5" width="220" height="9" />
              <rect x="20" y="10" rx="5" ry="5" width="40" height="40" />
            </ContentLoader>
          </p>
        {/each}
      {/if}
    </Card>
  </Container>
</Page>

<style>
/* .slider {
  width: 80%;
  margin: 0 auto;
}

@media (max-width: 800px) {
  .slider {
    width: 100%;
  }
} */

.slider {
  width: 100%;
}
</style>
