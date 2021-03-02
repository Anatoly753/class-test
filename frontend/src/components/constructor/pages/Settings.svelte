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
  mdiMinus,
  mdiNumeric,
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
import TextField from "svelte-materialify/src/components/TextField";
import Textarea from "svelte-materialify/src/components/Textarea";
import { text } from "svelte/internal";
import { sub } from "date-fns";
import { subjectItems, subjects } from "../../../api/test_subjectItems.js";

const dispatch = createEventDispatcher();

let snackbar = false;
let errorMessage = "";

let disabled = false;
let title = "";
let description = " ";
let subject = "";
let classNumber = 1;

function error(err) {
  errorMessage = err;
  snackbar = true;
  disabled = false;
}

onMount(() => {
  title = test.title;
  description = test.description;
  subject = subjectItems.find((item) => item.key === test.subject).name;
  classNumber = test.classNumber;
});

async function save_changes() {
  disabled = true;

  if (title.length == 0 || title.length > 150)
    return error("Вы не заполнили название теста или оно слишком длинное");
  if (description.length > 255) return error("Cлишком длинное описание");
  if (subject == "") return error("Нужно выбрать предмет");

  axios({
    method: "put",
    url: `APP.API/tests/${params.id}`,
    headers: {
      "X-Requested-With": "XMLHttpRequest",
      "Content-Type": "application/json",
      Authorization: `Token ${$user.token}`,
    },
    data: {
      test: {
        settings: JSON.stringify(test.settings),
      },
    },
    withCredentials: true,
  })
    .then((res) => {
      test = res.data.test;

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

      dispatch("message", {
        text: "Изменения сохранены",
        type: "success",
        test: test,
      });

      goto(`/constructor/editor/${test.id}/home`);
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
      disabled = false;
    });
}

const titleRules = [
  (v) => (v.length > 0 && length <= 150) || "Не может быть пустым",
  (v) => v.length <= 150 || "Максимум 150 символов",
];

const descriptionRules = [(v) => v.length <= 255 || "Максимум 255 символов"];

export let params;
export let test;
</script>

<!-- <div class="d-flex justify-center" style="width: 100%;"> -->
<div style="max-width: 400px;">
  <TextField
    class="mt-2 mb-2"
    outlined
    type="number"
    bind:value={test.settings.test.maxScore}>
    <div slot="prepend">
      <Icon path={mdiNumeric} />
    </div>
    Максимальное количество баллов за тест
  </TextField>

  <div style="min-width: 100%;" class="d-flex justify-center">
    <Button
      class="primary-color"
      on:click={save_changes}
      disabled={disabled}
      rounded
      size="large">
      {#if disabled == true}
        <Icon spin path={mdiLoading} />
        Загрузка...
      {:else}
        <Icon path={mdiContentSaveEditOutline} />
        Сохранить изменения
      {/if}
    </Button>
  </div>
</div>
<!-- </div> -->

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
