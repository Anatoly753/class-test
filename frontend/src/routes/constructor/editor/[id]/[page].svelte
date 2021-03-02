<script context="module">
export async function preload({ params }) {
  return { params: params };
}
</script>

<script>
import Page from "../../../../components/Page.svelte";
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
  Tabs,
  Tab,
  TabContent,
  Snackbar,
} from "svelte-materialify/src";
import ExpansionPanels, {
  ExpansionPanel,
} from "svelte-materialify/src/components/ExpansionPanels";
import {
  mdiSchoolOutline,
  mdiClockOutline,
  mdiFlagCheckered,
  mdiPlus,
  mdiDeleteSweepOutline,
  mdiCog,
  mdiBookArrowDownOutline,
  mdiBookArrowUpOutline,
  mdiContentSaveEditOutline,
  mdiLoading,
  mdiHomeCity,
  mdiHomeCityOutline,
  mdiInformationVariant,
  mdiFrequentlyAskedQuestions,
  mdiCogs,
  mdiChartBar,
  mdiFormatAnnotationPlus,
  mdiDesktopMacDashboard,
  mdiConsoleLine,
} from "@mdi/js";
import ContentLoader from "svelte-content-loader";
import { user } from "../../../_store.js";
import { goto } from "@sapper/app";
import { fade, slide } from "svelte/transition";
import { onMount } from "svelte";
import axios from "axios";
import formatDistanceToNow from "date-fns/formatDistanceToNow";
import format from "date-fns/format";
import { ru, te } from "date-fns/locale";
import Editor from "cl-editor";
import arrayMove from "array-move";
import NavigationDrawer from "svelte-materialify/src/components/NavigationDrawer";
import Avatar from "svelte-materialify/src/components/Avatar";
import Home from "../../../../components/constructor/pages/Home.svelte";
import Info from "../../../../components/constructor/pages/Info.svelte";
import Questions from "../../../../components/constructor/pages/Questions.svelte";
import Settings from "../../../../components/constructor/pages/Settings.svelte";
import { text } from "svelte/internal";

let snackbar = false;
let messageText = "";
let messageType = "";

let disabled = false;

let loading = true;
let test;
let questions = [];
let answers = [];
let currentQuestion = 0;

let editor;

let w;

onMount(async () => {
  findTest();
});

function msg(text, type) {
  messageText = text;
  messageType = type;
  snackbar = true;
  disabled = false;
}

async function findTest() {
  axios({
    method: "get",
    url: `APP.API/tests/answers/${params.id}`,
    headers: {
      "X-Requested-With": "XMLHttpRequest",
      "Content-Type": "application/json",
      Authorization: `Token ${$user.token}`,
    },
    withCredentials: true,
  })
    .then((res) => {
      test = res.data.test;
      console.log(test);

      test.questions =
        typeof test?.questions == "string" && test?.questions.length > 0
          ? JSON.parse(test.questions)
          : [];
      test.answers =
        typeof test?.answers == "string" && test?.answers.length > 0
          ? JSON.parse(test.answers)
          : [];
      test.settings =
        typeof test?.settings == "string" && test?.settings.length > 0
          ? JSON.parse(test.settings)
          : {};
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

let mini = true;
function mouseenter() {
  mini = false;
}
function mouseleave() {
  mini = true;
}

export let params;
</script>

<svelte:head>
  <title>Редактор тестов</title>
</svelte:head>

<Page authenticationRequired>
  <Container style="max-width: 1400px;">
    <Card class="pa-4">
      {#if test != undefined}
        <!-- <h5 class="text-center">{test.title}</h5>
        <hr
          class="mt-1 mb-4"
          style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" /> -->
        <div
          style="position: absolute; z-index: 4; height: calc( 100% - 30px ); min-height: 400px;"
          on:mouseenter={mouseenter}
          on:mouseleave={mouseleave}>
          <NavigationDrawer
            mini={mini}
            width={mini == true ? 56 : 255}
            class="rounded primary-color white-text">
            <ListItem class="white-text">
              <span slot="prepend" class="ml-n2">
                <Avatar size={40}>
                  <img
                    src="/editor/editor-min.png"
                    alt="Editor"
                    style="width: 40px; height: 40px;" />
                </Avatar>
              </span>
              Редактор тестов
            </ListItem>
            <Divider class="white-text" />
            <List dense nav class="white-text">
              <ListItem
                active={params.page == "home"}
                on:click={() => {
                  goto(`/constructor/editor/${params.id}/home`, {
                    scroll: true,
                  });
                }}>
                <span slot="prepend">
                  <Icon path={mdiDesktopMacDashboard} class="white-text" />
                </span>
                <b class="pl-3 white-text">Главная</b>
              </ListItem>

              <ListItem
                active={params.page == "info"}
                on:click={() => {
                  goto(`/constructor/editor/${params.id}/info`, {
                    scroll: true,
                  });
                }}>
                <span slot="prepend">
                  <Icon path={mdiInformationVariant} class="white-text" />
                </span>
                <b class="pl-3 white-text">Информация</b>
              </ListItem>

              <ListItem
                active={params.page == "questions"}
                on:click={() => {
                  goto(`/constructor/editor/${params.id}/questions`, {
                    scroll: true,
                  });
                }}>
                <span slot="prepend">
                  <Icon path={mdiFrequentlyAskedQuestions} class="white-text" />
                </span>
                <b class="pl-3 white-text">Вопросы</b>
              </ListItem>

              <ListItem
                active={params.page == "settings"}
                on:click={() => {
                  goto(`/constructor/editor/${params.id}/settings`, {
                    scroll: true,
                  });
                }}>
                <span slot="prepend">
                  <Icon path={mdiCogs} class="white-text" />
                </span>
                <b class="pl-3 white-text">Настройки</b>
              </ListItem>

              <ListItem
                active={params.page == "statistics"}
                on:click={() => {
                  goto(`/constructor/editor/${params.id}/statistics`, {
                    scroll: true,
                  });
                }}>
                <span slot="prepend">
                  <Icon path={mdiChartBar} class="white-text" />
                </span>
                <b class="pl-3 white-text">Статистика</b>
              </ListItem>

              <ListItem
                active={params.page == "results"}
                on:click={() => {
                  goto(`/constructor/editor/${params.id}/results`, {
                    scroll: true,
                  });
                }}>
                <span slot="prepend">
                  <Icon path={mdiFormatAnnotationPlus} class="white-text" />
                </span>
                <b class="pl-3 white-text">Результаты</b>
              </ListItem>
            </List>
          </NavigationDrawer>
        </div>

        <div class="page" style="margin-left: 64px; min-height: 400px;">
          {#if params.page == "home"}
            <Home test={test} params={params} />
          {:else if params.page == "info"}
            <Info
              test={test}
              params={params}
              on:message={(event) => {
                if (event.detail.test != undefined) test = event.detail.test;
                msg(event.detail.text, event.detail.type);
              }} />
          {:else if params.page == "questions"}
            <Questions
              test={test}
              params={params}
              on:message={(event) => {
                if (event.detail.test != undefined) test = event.detail.test;
                msg(event.detail.text, event.detail.type);
              }} />
          {:else if params.page == "settings"}
            <Settings
              test={test}
              params={params}
              on:message={(event) => {
                if (event.detail.test != undefined) test = event.detail.test;
                msg(event.detail.text, event.detail.type);
              }} />
          {/if}
        </div>
      {:else}Loading...{/if}
    </Card>
  </Container>

  <Snackbar
    class="justify-space-between {messageType == 'success'
      ? 'success-color'
      : ''}"
    bind:active={snackbar}
    right
    top
    timeout={5000}>
    <b>{messageText}</b>
    <Button
      text
      on:click={() => {
        snackbar = false;
      }}>Закрыть</Button>
  </Snackbar>
</Page>
