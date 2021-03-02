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
  Textarea,
  Snackbar,
} from "svelte-materialify/src";
import ExpansionPanels, {
  ExpansionPanel,
} from "svelte-materialify/src/components/ExpansionPanels";
import {
  mdiPlus,
  mdiMinus,
  mdiFileSearchOutline,
  mdiCommentArrowRightOutline,
  mdiLoading,
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

let subjectItems = [
  { name: "Математика", value: "Математика", key: "mathematics" },
  { name: "Геометрия", value: "Геометрия", key: "geometry" },
  { name: "Физика", value: "Физика", key: "physics" },
  { name: "Русский язык", value: "Русский язык", key: "russian" },
  { name: "Информатика", value: "Информатика", key: "informatics" },
  { name: "История", value: "История", key: "history" },
  { name: "Английский язык", value: "Английский язык", key: "english" },
  { name: "Литература", value: "Литература", key: "literature" },
  { name: "Химия", value: "Химия", key: "chemistry" },
  { name: "Биология", value: "Биология", key: "biology" },
  { name: "География", value: "География", key: "geography" },
  { name: "Обществознание", value: "Обществознание", key: "social_studies" },
];

let snackbar = false;
let errorMessage = "";

let disabled = false;
let test;
let title = "";
let description = "";
let subject = "";
let classNumber = 1;

function error(err) {
  errorMessage = err;
  snackbar = true;
  disabled = false;
}

// onMount(async () => {
//   // findTest();
// });

async function create_test_and_next() {
  disabled = true;

  if (title.length == 0 || title.length > 150)
    return error("Вы не заполнили название теста или оно слишком длинное");
  if (description.length > 255) return error("Cлишком длинное описание");
  if (subject == "") return error("Нужно выбрать предмет");

  axios({
    method: "post",
    url: `APP.API/tests/`,
    headers: {
      "X-Requested-With": "XMLHttpRequest",
      "Content-Type": "application/json",
      Authorization: `Token ${$user.token}`,
    },
    data: {
      test: {
        title: title,
        description: description,
        subject: subjectItems.find((item) => item.name === subject).key,
        classNumber: classNumber,
        questions: "[]",
        answers: "[]",
        settings: JSON.stringify({ test: {}, questions: [] }),
      },
    },
    withCredentials: true,
  })
    .then((res) => {
      test = res.data.test;
      goto(`/constructor/editor/${test.id}/home`, { scroll: true });
    })
    .catch((error) => {
      console.log(error);
      error(error.message);
      if (error.response) {
        // requestErrorMessage = JSON.stringify(error.response.data.errors);
        // requestError = !requestError;
      } else {
        // requestErrorMessage = error.message;
        // requestError = !requestError;
      }
    })
    .finally(() => {
      disabled = false;
    });
}

const titleRules = [
  (v) => (v.length > 0 && length <= 150) || "Не может быть пустым",
  (v) => v.length <= 150 || "Максимум 150 символов",
];

const descriptionRules = [(v) => v.length <= 255 || "Максимум 255 символов"];

// export let params;
</script>

<svelte:head>
  {#if test != undefined}
    <title>{test.title}</title>
  {:else}
    <title>Пройти тест</title>
  {/if}
</svelte:head>

<Page authenticationRequired>
  <Container style="max-width:800px;">
    <Card class="pa-4">
      <h4 class="text-center">Новый тест</h4>
      <hr
        class="mt-1 mb-4"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />
      <div style="max-width: 800px; margin: 0 auto;">
        <TextField
          bind:value={title}
          clearable
          outlined
          counter="150"
          maxlength="150"
          rules={titleRules}
          class="mt-2">
          <div slot="prepend">
            <Icon path={mdiFileSearchOutline} />
          </div>
          Название теста
        </TextField>

        <Textarea
          bind:value={description}
          rules={descriptionRules}
          outlined
          counter={255}
          autogrow
          rows={2}>Описание(необязательно)</Textarea>

        <div style="max-width: 250px; margin: 0 auto;">
          <Select outlined items={subjectItems} bind:value={subject}>
            Предмет
          </Select>
        </div>

        <p class="text-center" style="height: 35px;">
          <b>Класс: {classNumber} </b>
        </p>
        <div class="slider">
          <Slider
            thumb
            persistentThumb
            min={1}
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

        <hr
          class="mt-3 mb-4"
          style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

        <div style="min-width: 100%;" class="d-flex justify-center">
          <Button
            class="primary-color"
            on:click={create_test_and_next}
            disabled={disabled}
            rounded
            size="large">
            {#if disabled == true}
              <Icon spin path={mdiLoading} />
              Загрузка...
            {:else}
              <Icon path={mdiCommentArrowRightOutline} />
              Дальше
            {/if}
          </Button>
        </div>
      </div>
    </Card>
  </Container>
  <Snackbar
    class="justify-space-between"
    bind:active={snackbar}
    right
    top
    timeout={3000}>
    {errorMessage}
    <Button
      text
      on:click={() => {
        snackbar = false;
      }}>Закрыть</Button>
  </Snackbar>
</Page>
