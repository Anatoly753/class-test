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
  Tabs,
  Tab,
  TabContent,
  Snackbar,
  Row,
  Col,
  Dialog,
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
  mdiInformationVariant,
  mdiFrequentlyAskedQuestions,
  mdiCogs,
  mdiChartBar,
  mdiFormatAnnotationPlus,
  mdiWrench,
  mdiLinkVariant,
  mdiCommentQuestionOutline,
  mdiFormatTitle,
  mdiImageText,
  mdiDeleteAlertOutline,
  mdiFileQuestionOutline,
  mdiFileCogOutline,
  mdiContentCopy,
} from "@mdi/js";
import ContentLoader from "svelte-content-loader";
import { user } from "../../../routes/_store.js";
import { goto } from "@sapper/app";
import { fade, slide } from "svelte/transition";
import { onMount, createEventDispatcher } from "svelte";
import axios from "axios";
import formatDistanceToNow from "date-fns/formatDistanceToNow";
import format from "date-fns/format";
import { ru } from "date-fns/locale";
import Editor from "cl-editor";
import arrayMove from "array-move";
import Chart from "svelte-frappe-charts";
import { subjectItems, subjects } from "../../../api/test_subjectItems.js";

const dispatch = createEventDispatcher();

let deleteTestId;

let deleteDialogActive = false;
let deleteButtonDisabled = true;
let deleteLoading = false;
let deleteTimerId;
let deleteTimer = 5;

onMount(async () => {});

async function delete_test() {
  deleteButtonDisabled = true;
  deleteLoading = true;

  axios({
    method: "delete",
    url: `APP.API/tests/${test.id}`,
    headers: {
      "X-Requested-With": "XMLHttpRequest",
      "Content-Type": "application/json",
      Authorization: `Token ${$user.token}`,
    },
    withCredentials: true,
  })
    .then((res) => {
      goto(`/constructor/tests`);
    })
    .catch((error) => {
      console.log(error);
      dispatch("message", {
        text: error.message,
        type: "error",
      });
      if (error.response) {
        // requestErrorMessage = JSON.stringify(error.response.data.errors);
        // requestError = !requestError;
      } else {
        // requestErrorMessage = error.message;
        // requestError = !requestError;
      }
    })
    .finally(() => {
      deleteLoading = false;
      deleteDialogActive = false;
    });
}

async function copy_link() {
  try {
    const el = document.createElement("textarea");
    el.value = `APP.URL/test/view/${test.id}`;
    el.setAttribute("readonly", "");
    el.style.position = "absolute";
    el.style.left = "-9999px";
    document.body.appendChild(el);
    el.select();
    document.execCommand("copy");
    document.body.removeChild(el);
    alert("Сcылка на тест скопирована в буфер обмена");
    dispatch("message", {
      text: "Сcылка на тест скопирована в буфер обмена",
      type: "success",
    });
  } catch (err) {
    alert("Не удалось скопировать ссылку");
    dispatch("message", {
      text: "Не удалось скопировать ссылку",
      type: "error",
    });
  }
}

export let params;
export let test;
</script>

<Row noGutters>
  <Col cols={12} sm={6} md={4}>
    <Card class="mb-2 mr-2 pb-2">
      <h5 class="text-center primary-color white-text pt-1 pb-1 mb-3">
        <Icon path={mdiFileQuestionOutline} class="white-text" />
        Тест
      </h5>

      <p>
        <b class="ml-4"
          ><Icon path={mdiFormatTitle} />
          Название:
          {test.title}</b>
      </p>
      <p>
        <b class="ml-4"><Icon path={mdiImageText} />Описание:</b>
      </p>
      <div class="ml-8 mb-4">
        {@html test.description.replace(/\n/g, " <br /> ")}
      </div>
      <p>
        <b class="ml-4"
          ><Icon path={mdiSchoolOutline} class="default-icon" />
          Класс:
          {test.classNumber}</b>
      </p>
      <p>
        <b class="ml-4">
          <Icon path={mdiClockOutline} class="default-icon" />
          Добавлен:
          {formatDistanceToNow(new Date(test.createdAt.split("[")[0]), {
            addSuffix: true,
            locale: ru,
          })}
        </b>
      </p>
      <!-- Оценка -->
      <!-- Мои результаты(таблица) -->
      <p>
        <b class="ml-4">
          <Icon path={mdiCommentQuestionOutline} class="default-icon" />
          Вопросы:
          {#if Array.isArray(test.questions) == true}
            {test.questions.length}
          {:else}
            0
          {/if}
        </b>
      </p>
      <p>
        <b class="ml-4"
          ><Icon path={mdiLinkVariant} class="default-icon" />
          <a target="_blank" href={`/test/view/${test.id}`}>Сcылка на тест</a>
          <Button
            class="primary-color"
            on:click={copy_link}
            rounded
            fab
            size="small"
            ><Icon class="default-icon" path={mdiContentCopy} /></Button>
        </b>
      </p>

      <p>
        <a class="ml-4" href={`/constructor/editor/${params.id}/info`}
          ><Icon path={mdiInformationVariant} /><b
            >Редактировать информацию о тесте</b
          ></a>
      </p>
    </Card>
  </Col>
  <Col cols={12} sm={6} md={4}>
    <Card class="mb-2 mr-2 pb-2">
      <h5 class="text-center primary-color white-text pt-1 pb-1 mb-3">
        <Icon path={mdiWrench} class="white-text" />
        Управление
      </h5>

      <p>
        <a class="ml-4" href={`/constructor/editor/${params.id}/questions`}
          ><Icon path={mdiFrequentlyAskedQuestions} /><b
            >Редактировать вопросы</b
          ></a>
      </p>

      <p>
        <a class="ml-4" href={`/constructor/editor/${params.id}/settings`}
          ><Icon path={mdiFileCogOutline} /><b>Настройки теста</b></a>
      </p>

      <p>
        <a class="ml-4" href={`/constructor/editor/${params.id}/results`}
          ><Icon path={mdiFormatAnnotationPlus} /><b>Результаты</b></a>
      </p>

      <!-- <hr
        class="mt-1 mb-4"
        style="border: 0; height: 1px; background-image: linear-gradient(to
            right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" /> -->

      <p>
        <b
          class="ml-4 error-text delete"
          on:click={() => {
            deleteDialogActive = true;
            deleteTimerId = setInterval(() => {
              deleteTimer--;
              if (deleteTimer == 0) {
                clearInterval(deleteTimerId);
                deleteButtonDisabled = false;
              }
            }, 1000);
          }}><Icon path={mdiDeleteAlertOutline} class="error-text" />Удалить</b>
      </p>
    </Card>
  </Col>
  <Col cols={12} sm={6} md={4}>
    <!-- <div class="pa-2">.col-12.col-sm-6.col-md-4</div> -->
    <Card class="mb-2 mr-2 pb-2">
      <h5 class="text-center primary-color white-text pt-1 pb-1 mb-3">
        <Icon path={mdiChartBar} class="white-text" />
        Статистика
      </h5>

      <p>
        <a class="ml-4" href={`/constructor/editor/${params.id}/statistics`}
          ><Icon path={mdiChartBar} /><b>Подробнее</b></a>
      </p>
      <p>ChartJS pie chart</p>
    </Card>
  </Col>
</Row>

<Dialog persistent bind:active={deleteDialogActive}>
  <Card>
    <CardTitle>
      <h5 class="text-center">Вы уверены, что хотите удалить тест?</h5>
    </CardTitle>
    <!-- <CardText>
      Lorem ipsum, dolor sit amet consectetur adipisicing elit. Mollitia
      deleniti natus dolore, rerum hic beatae officiis at ea sequi labore.
    </CardText> -->
    <CardActions>
      <div
        style="width: 80%; margin: 0 auto;"
        class="d-flex justify-space-between">
        <Button
          class="red white-text"
          on:click={delete_test}
          bind:disabled={deleteButtonDisabled}
          rounded
          size="large">
          {#if deleteLoading == true}
            <Icon spin path={mdiLoading} />
            Загрузка...
          {:else}
            <Icon path={mdiDeleteAlertOutline} />
            {#if deleteTimer <= 0}Да, удалить{:else}{deleteTimer}{/if}
          {/if}
        </Button>
        <Button
          on:click={() => {
            clearInterval(deleteTimerId);
            deleteTestId = undefined;
            deleteButtonDisabled = true;
            deleteLoading = false;
            deleteTimer = 5;
            deleteDialogActive = false;
          }}
          rounded
          size="large"
          class="primary-color white-text">Отмена</Button>
      </div>
    </CardActions>
  </Card>
</Dialog>

<style>
a {
  text-decoration: none;
  color: #333333;
}

a:hover,
.delete:hover {
  text-decoration: underline;
  cursor: pointer;
}
</style>
