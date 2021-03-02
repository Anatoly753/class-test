<script context="module">
export async function preload({ params }) {
  return { params: params };
}
</script>

<script>
import Page from "../../../components/Page.svelte";
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
  mdiCommentQuestionOutline,
  mdiImageText,
} from "@mdi/js";
import ContentLoader from "svelte-content-loader";
import { user } from "../../_store.js";
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

let loading = true;
let test;

onMount(async () => {
  findTest();
});

async function findTest() {
  axios({
    method: "get",
    url: `APP.API/tests/${params.id}`,
    headers: {
      "X-Requested-With": "XMLHttpRequest",
      "Content-Type": "application/json",
    },
    withCredentials: true,
  })
    .then((res) => {
      test = res.data.test;
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

export let params;
</script>

<svelte:head>
  {#if test != undefined}
    <title>{test.title}</title>
  {:else}
    <title>Пройти тест</title>
  {/if}
</svelte:head>

<Page>
  <Container style="max-width:800px;">
    <Card class="pa-4">
      {#if test != undefined}
        <h4 class="text-center">{test.title}</h4>
        <hr
          class="mt-1 mb-4"
          style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

        <!-- <div class="d-flex justify-center"> -->
        <p>
          <Icon path={mdiSchoolOutline} class="default-icon" />
          {test.classNumber}
          класс
        </p>
        <p>
          <Icon path={mdiClockOutline} class="default-icon" />
          Добавлен:
          {formatDistanceToNow(new Date(test.createdAt.split("[")[0]), {
            addSuffix: true,
            locale: ru,
          })}
        </p>
        <!-- Оценка -->
        <!-- Мои результаты(таблица) -->
        <p>
          <Icon path={mdiImageText} class="default-icon" />
          Вопросы:
          {JSON.parse(test.questions).length}
        </p>

        <p>
          <Icon
            path={mdiCommentQuestionOutline}
            class="default-icon" />Описание:
        </p>
        <blockquote style="margin-top: -28px;">
          {@html test.description.replace(/\n/g, " <br /> ")}
        </blockquote>

        <Button
          class="primary-color"
          on:click={() => {
            goto(`/test/solve/${params.id}`, { scroll: true });
          }}
          rounded
          size="large">
          <Icon path={mdiFlagCheckered} />
          Пройти тест
        </Button>
        <!-- </div> -->
      {:else}
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
