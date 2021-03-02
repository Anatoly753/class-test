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
  mdiContentCopy,
  mdiNumeric,
  mdiText,
  mdiChevronDown,
  mdiDeleteCircleOutline,
  mdiDeleteOutline,
  mdiFormatListBulletedType,
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
import { subjectItems, subjects } from "../../../api/test_subjectItems.js";
import Window from "svelte-materialify/src/components/Window";
import WindowItem from "svelte-materialify/src/components/Window/WindowItem.svelte";
import Tooltip from "svelte-materialify/src/components/Tooltip";
import Avatar from "svelte-materialify/src/components/Avatar";
import { answerInputTypes } from "../../../api/test_answerInputTypes.js";
import Row from "svelte-materialify/src/components/Grid/Row.svelte";
import Col from "svelte-materialify/src/components/Grid/Col.svelte";
import Checkbox from "svelte-materialify/src/components/Checkbox";
import CardText from "svelte-materialify/src/components/Card/CardText.svelte";
import TextField from "svelte-materialify/src/components/TextField";

const dispatch = createEventDispatcher();

let snackbar = false;
let errorMessage = "";

let disabled = false;

let loading = true;
let currentQuestion = 0;

let text_or_number_columns = ["Ответ", "Баллы", /*"Верный?",*/ ""];
let currentAnswers = [
  {
    answer: "Apple",
    score: 1,
  },
  {
    answer: "Blueberry",
    score: 2,
  },
  {
    answer: "Tomato",
    score: 0,
  },
];

let newAnswer = {
  answer: "",
  score: 0,
  right: false,
};

let editor;

let answerInputManagerActive = false;
let answerType;
// $: if (test?.settings?.questions[currentQuestion]?.answerType != undefined) {
$: answerType = test?.settings?.questions?.[currentQuestion]?.answerType;
// }
let answerTypeParams;
$: answerTypeParams =
  test?.settings?.questions?.[currentQuestion]?.answerParams || {};
let activeAnswerInputSettings = false;

$: debug = JSON.stringify(test, null, 2);

onMount(async () => {});

function toggleAnswerInputSettings() {
  activeAnswerInputSettings = !activeAnswerInputSettings;
}

function error(err) {
  errorMessage = err;
  snackbar = true;
  disabled = false;
}

function save_currentAnswers() {
  test.answers[currentQuestion] = currentAnswers;
}

function saveAnswerParamsForCurrentQuestion() {
  // console.log(answerTypeParams);
  setSettingForCurrentQuestion("answerParams", answerTypeParams);
  // test.settings.questions[currentQuestion].answerParams = answerTypeParams;
  // if (typeof test.settings != "object") {
  //   test.settings = {
  //     test: {},
  //     questions: {},
  //   };
}

function setSettingForCurrentQuestion(setting, value) {
  if (typeof test.settings != "object") {
    test.settings = {
      test: {},
      questions: [],
    };
  }

  if (!Array.isArray(test.settings.questions)) test.settings.questions = [];

  // console.log(typeof test.settings.questions[currentQuestion]);

  // console.log(typeof test.settings.questions);

  if (typeof test.settings.questions.length != "object")
    if (typeof test.settings.questions[currentQuestion] != "object") {
      console.log(test.settings.questions);

      test.settings.questions = insert(
        test.settings.questions,
        currentQuestion,
        {}
      );

      console.log(test.settings.questions);
    }
  // test.settings.questions[currentQuestion] = {};

  // console.log(Array.isArray(test.settings.questions));

  test.settings.questions[currentQuestion][setting] = value;
}

async function save_changes() {
  disabled = true;

  saveAnswerParamsForCurrentQuestion();

  // test.questions = JSON.stringify(questions);

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
        questions: JSON.stringify(test.questions),
        answers: JSON.stringify(test.answers),
        settings: JSON.stringify(test.settings),
      },
    },
    withCredentials: true,
  })
    .then((res) => {
      // console.log(test.answers);
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

let editorActions = [
  "undo",
  "redo",
  // {
  //   name: "answer",
  //   icon:
  //     '<img src="/icons/comment-check-outline.svg" style="vertical-align: middle;width:21px;height:21px" />',
  //   title: "Поле ответа",
  //   result: () => {
  //     answerInputManagerActive = true;
  //     //dialog with tab or выпадающий список
  //   },
  // },
  "b",
  "i",
  "u",
  "strike",
  "sup",
  "sub",
  "h1",
  "h2",
  {
    name: "h3",
    icon: "<b>H<sub>3</sub></b>",
    title: "Heading 3",
    result: () => editor.exec("formatBlock", "<H3>"),
  },
  {
    name: "h4",
    icon: "<b>H<sub>4</sub></b>",
    title: "Heading 4",
    result: () => editor.exec("formatBlock", "<H4>"),
  },
  {
    name: "h5",
    icon: "<b>H<sub>5</sub></b>",
    title: "Heading 5",
    result: () => editor.exec("formatBlock", "<H5>"),
  },
  {
    name: "h6",
    icon: "<b>H<sub>6</sub></b>",
    title: "Heading 6",
    result: () => editor.exec("formatBlock", "<H6>"),
  },
  "p",
  "blockquote",
  "ol",
  "ul",
  "hr",
  "left",
  "right",
  "center",
  "justify",
  "a",
  "image",
  "forecolor",
  "backcolor",
  "removeFormat",
];

const insert = (arr, index, newItem) => [
  ...arr.slice(0, index),
  newItem,
  ...arr.slice(index),
];

export let params;
export let test;
</script>

<!-- <Page authenticationRequired>
  <Container style="max-width:1200px;"> -->
<Card class="pa-4">
  <h5 class="text-center">Конструктор вопросов</h5>
  <hr
    class="mt-1 mb-4"
    style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />
  <div class="wrapper">
    <div class="questionsBar">
      {#if test != undefined && test.questions.length > 0}
        <Tabs
          bind:value={currentQuestion}
          vertical
          on:change={() => {
            editor.setHtml(test.questions[currentQuestion]);
            saveAnswerParamsForCurrentQuestion();
          }}>
          <div slot="tabs">
            {#each test.questions || [] as _, i}
              <Tab><b>{i + 1}</b></Tab>
            {/each}
          </div>
        </Tabs>
        <hr
          class="mt-4"
          style="border: 0; height: 1px; background: rgba(100, 100, 100, 0.4);" />
      {/if}

      <Button
        class="primary-color mt-4 mb-4"
        style="display: block; margin: 0 auto;"
        size="small"
        fab
        on:click={() => {
          test.questions.push("");
          test = test;

          if (typeof test.settings != "object") {
            test.settings = {
              test: {},
              questions: [],
            };
          }

          if (!Array.isArray(test.settings.questions))
            test.settings.questions = [];

          // console.log(typeof test.settings.questions[currentQuestion]);

          // console.log(typeof test.settings.questions);

          // if (typeof test.settings.questions.length != "object")
          //   if (typeof test.settings.questions[currentQuestion] != "object") {
          //     console.log(test.settings.questions);

          //     test.settings.questions = insert(
          //       test.settings.questions,
          //       currentQuestion,
          //       {}
          //     );

          //     console.log(test.settings.questions);
          //   }
          // test.settings.questions[currentQuestion] = {};

          // console.log(Array.isArray(test.settings.questions));

          // test.settings.questions[currentQuestion][setting] = value;

          test.settings.questions = insert(
            test.settings.questions,
            test.questions.length - 1,
            {}
          );
          // let c = questions.length - 1;
          // currentQuestion = c;
          // if (editor != undefined) editor.setHtml(questions[currentQuestion]);

          if (typeof test.settings != "object") {
            test.settings = {
              test: {},
              questions: [],
            };
          }

          if (!Array.isArray(test.settings.questions))
            test.settings.questions = [];

          // console.log(typeof test.settings.questions[currentQuestion]);

          // console.log(typeof test.settings.questions);

          if (typeof test.settings.questions.length != "object")
            if (typeof test.settings.questions[currentQuestion] != "object") {
              console.log(test.settings.questions);

              test.settings.questions = insert(
                test.settings.questions,
                currentQuestion,
                {}
              );

              console.log(test.settings.questions);
            }
        }}>
        <Icon class="default-icon" path={mdiPlus} />
      </Button>
    </div>
    {#if test != undefined && test.questions.length > 0}
      <div class="toolBar">
        <Button
          class="primary-color"
          size="small"
          fab
          on:click={() => {
            if (currentQuestion == 0) return;
            test.questions = arrayMove(
              test.questions,
              currentQuestion,
              currentQuestion - 1
            );

            test.settings.questions = arrayMove(
              test.settings.questions,
              currentQuestion,
              currentQuestion - 1
            );

            test.answers = arrayMove(
              test.answers,
              currentQuestion,
              currentQuestion - 1
            );

            --currentQuestion;
          }}>
          <Icon class="default-icon" path={mdiBookArrowUpOutline} />
        </Button>

        <Button
          class="primary-color"
          size="small"
          fab
          on:click={() => {
            // test.questions.splice(currentQuestion, 1);
            if (currentQuestion == test.questions.length - 1) return;

            test.questions = arrayMove(
              test.questions,
              currentQuestion,
              currentQuestion + 1
            );

            test.settings.questions = arrayMove(
              test.settings.questions,
              currentQuestion,
              currentQuestion + 1
            );

            test.answers = arrayMove(
              test.answers,
              currentQuestion,
              currentQuestion + 1
            );

            ++currentQuestion;
          }}>
          <Icon class="default-icon" path={mdiBookArrowDownOutline} />
        </Button>

        <Button
          class="primary-color"
          size="small"
          fab
          on:click={() => {
            test.questions = insert(
              test.questions,
              currentQuestion + 1,
              test.questions[currentQuestion]
            );

            test.settings.questions = insert(
              test.settings.questions,
              currentQuestion + 1,
              test.settings.questions[currentQuestion]
            );

            test.answers = insert(
              test.answers,
              currentQuestion + 1,
              test.answers[currentQuestion]
            );
            if (currentQuestion == test.questions.length - 2) return;
            else ++currentQuestion;
          }}>
          <Icon class="default-icon" path={mdiContentCopy} />
        </Button>

        <Button
          class="primary-color"
          size="small"
          fab
          on:click={() => {
            if (
              confirm(
                `Вы уверены, что хотите удалить вопрос №${
                  currentQuestion + 1
                } из теста?`
              )
            ) {
              test.questions.splice(currentQuestion, 1);
              test.answers.splice(currentQuestion, 1);
              test.settings.questions.splice(currentQuestion, 1);
              if (currentQuestion > 0) currentQuestion--;
            }
            test.questions = test.questions;
            editor.setHtml(test.questions[currentQuestion]);
          }}>
          <Icon class="default-icon" path={mdiDeleteSweepOutline} />
        </Button>

        <Button
          class="primary-color"
          size="small"
          fab
          on:click={() => {
            alert("qsettings");
          }}>
          <Icon class="default-icon" path={mdiCog} />
        </Button>
      </div>
      <div class="editor">
        <Editor
          bind:this={editor}
          html={test.questions[currentQuestion]}
          actions={editorActions}
          on:change={(e) => (test.questions[currentQuestion] = e.detail)}
          height={"500px"} />
        <!-- {@html xss(test.questions[currentQuestion])} -->
      </div>

      <div class="answersBar">
        <!-- {JSON.stringify(test.settings)} -->
        <div class="answerTypeBlock">
          <h5 class="text-center white-text">Тип ответа</h5>
          <div class="d-flex justify-center">
            <Tooltip bottom>
              <span slot="tip">Поле ввода для текста</span>
              <div
                class={answerType == "text"
                  ? "answerType-active"
                  : "answerType"}
                on:click={() => {
                  answerType = "text";
                  setSettingForCurrentQuestion("answerType", "text");
                }}>
                <Avatar size={50} tile>
                  <img
                    src="/editor/answerComponents/text-min.png"
                    alt="Поле ввода для текста"
                    style="width: 50px; height: 50px;" />
                </Avatar>
              </div>
            </Tooltip>

            <Tooltip bottom>
              <span slot="tip">Поле ввода для чисел</span>
              <div
                class={answerType == "number"
                  ? "answerType-active"
                  : "answerType"}
                on:click={() => {
                  answerType = "number";
                  setSettingForCurrentQuestion("answerType", "number");
                }}>
                <Avatar size={50} tile>
                  <img
                    src="/editor/answerComponents/integer-min.png"
                    alt="Поле ввода для чисел"
                    style="width: 50px; height: 50px;" />
                </Avatar>
              </div>
            </Tooltip>

            <Tooltip bottom>
              <span slot="tip">Выбор одного ответа из списка</span>
              <div
                class={answerType == "select"
                  ? "answerType-active"
                  : "answerType"}
                on:click={() => {
                  answerType = "select";
                  setSettingForCurrentQuestion("answerType", "select");
                }}>
                <Avatar size={50} tile>
                  <img
                    src="/editor/answerComponents/select-min.png"
                    alt="Выбор одного ответа из списка"
                    style="width: 50px; height: 50px;" />
                </Avatar>
              </div>
            </Tooltip>

            <Tooltip bottom>
              <span slot="tip"
                >Множественный выбор одного ответа из списка</span>
              <div
                class={answerType == "multipleSelect"
                  ? "answerType-active"
                  : "answerType"}
                on:click={() => {
                  answerType = "multipleSelect";
                  setSettingForCurrentQuestion("answerType", "multipleSelect");
                }}>
                <Avatar size={50} tile>
                  <img
                    src="/editor/answerComponents/multipleSelect-min.png"
                    alt="Множественный выбор одного ответа из списка"
                    style="width: 50px; height: 50px;" />
                </Avatar>
              </div>
            </Tooltip>

            <!-- <Tooltip bottom>
              <span slot="tip">Выбор одного ответа из сетки</span>
              <div
                class={answerType == "selectGrid"
                  ? "answerType-active"
                  : "answerType"}
                on:click={() => {
                  answerType = "selectGrid";
                  setSettingForCurrentQuestion("answerType", "selectGrid");
                }}>
                <Avatar size={50} tile>
                  <img
                    src="/editor/answerComponents/selectGrid-min.png"
                    alt="Выбор одного ответа из сетки"
                    style="width: 50px; height: 50px;" />
                </Avatar>
              </div>
            </Tooltip>

            <Tooltip bottom>
              <span slot="tip">Множественный выбор одного ответа из сетки</span>
              <div
                class={answerType == "multipleSelectGrid"
                  ? "answerType-active"
                  : "answerType"}
                on:click={() => {
                  answerType = "multipleSelectGrid";
                  setSettingForCurrentQuestion(
                    "answerType",
                    "ьгдешздуЫelectGrid"
                  );
                }}>
                <Avatar size={50} tile>
                  <img
                    src="/editor/answerComponents/multipleSelectGrid-min.png"
                    alt="Множественный выбор одного ответа из сетки"
                    style="width: 50px; height: 50px;" />
                </Avatar>
              </div>
            </Tooltip> -->

            <Tooltip bottom>
              <span slot="tip">Выпадающий список</span>
              <div
                class={answerType == "dropDownList"
                  ? "answerType-active"
                  : "answerType"}
                on:click={() => {
                  answerType = "dropDownList";
                  setSettingForCurrentQuestion("answerType", "dropDownList");
                }}>
                <Avatar size={50} tile>
                  <img
                    src="/editor/answerComponents/dropDownList-min.png"
                    alt="Выпадающий список"
                    style="width: 50px; height: 50px;" />
                </Avatar>
              </div>
            </Tooltip>

            <Tooltip bottom>
              <span slot="tip">Слайдер</span>
              <div
                class={answerType == "slider"
                  ? "answerType-active"
                  : "answerType"}
                on:click={() => {
                  answerType = "slider";
                  setSettingForCurrentQuestion("answerType", "slider");
                }}>
                <Avatar size={50} tile>
                  <img
                    src="/editor/answerComponents/slider-min.png"
                    alt="Слайдер"
                    style="width: 50px; height: 50px;" />
                </Avatar>
              </div>
            </Tooltip>

            <Tooltip bottom>
              <span slot="tip">Диапазон</span>
              <div
                class={answerType == "range"
                  ? "answerType-active"
                  : "answerType"}
                on:click={() => {
                  answerType = "range";
                  setSettingForCurrentQuestion("answerType", "range");
                }}>
                <Avatar size={50} tile>
                  <img
                    src="/editor/answerComponents/range-min.png"
                    alt="Диапазон"
                    style="width: 50px; height: 50px;" />
                </Avatar>
              </div>
            </Tooltip>
          </div>
        </div>

        {#if answerType != undefined}
          <div class="answersInputSettings">
            <Card style="width: 100%;">
              <CardTitle class="primary-color white-text"
                >Настройки поля для ответа <Button
                  text
                  fab
                  size="small"
                  class="ml-auto"
                  on:click={toggleAnswerInputSettings}>
                  <Icon
                    path={mdiChevronDown}
                    rotate={activeAnswerInputSettings ? 180 : 0} />
                </Button></CardTitle>
              {#if activeAnswerInputSettings}
                <div transition:slide class="pl-4 pr-4 pt-2 pb-2">
                  {#each answerInputTypes as answerInputType}
                    {#if answerInputType.type == answerType}
                      {#each answerInputType.parameters as parameter}
                        {#if parameter.type == "integer"}
                          <TextField
                            class="mt-2 mb-2"
                            outlined
                            type="number"
                            bind:value={answerTypeParams[parameter.param]}>
                            <div slot="prepend">
                              <Icon path={mdiNumeric} />
                            </div>
                            {parameter.name}
                          </TextField>
                        {:else if parameter.type == "text"}
                          <TextField
                            class="mt-2 mb-2"
                            outlined
                            type="text"
                            bind:value={answerTypeParams[parameter.param]}>
                            <div slot="prepend">
                              <Icon path={mdiText} />
                            </div>
                            {parameter.name}
                          </TextField>
                        {:else if parameter.type == "checkbox"}
                          <Checkbox
                            bind:checked={answerTypeParams[parameter.param]}
                            >{parameter.name}</Checkbox>
                        {:else if parameter.type == "enum"}
                          <!-- <div style="max-width: 300px; margin: 0 auto;"> -->
                          <Select
                            outlined
                            items={parameter.enum}
                            bind:value={answerTypeParams[parameter.param]}>
                            {parameter.name}</Select>
                          <!-- </div> -->
                        {/if}
                      {/each}
                    {/if}
                  {/each}
                </div>
              {/if}
            </Card>
          </div>

          <div class="answers">
            <table>
              <thead>
                <tr>
                  {#each text_or_number_columns as column}
                    <th>{column}</th>
                  {/each}
                </tr>
              </thead>
              <tbody>
                {#if test.answers != undefined && Array.isArray(test.answers) && test.answers.length > 0 && Array.isArray(test.answers[currentQuestion]) && test.answers[currentQuestion].length > 0}
                  {#each test.answers[currentQuestion] || {} as row, i}
                    <tr>
                      <td contenteditable="true" bind:innerHTML={row.answer} />
                      <td
                        style="width: 50px; text-align: right;"
                        contenteditable="true"
                        bind:innerHTML={row.score} />
                      <!-- <td style="width: 30px;"
                        ><div
                          class="d-flex justify-center"
                          style="padding-left: 12px;">
                          <Checkbox bind:checked={row.right} />
                        </div></td> -->
                      <td style="width: 20px; text-align: center;">
                        <Button
                          class="primary-color"
                          on:click={() => {
                            if (
                              confirm("Вы уверены, что хотите удалить ответ?")
                            ) {
                              test.answers[currentQuestion].splice(i, 1);
                            }
                            test.answers[currentQuestion] =
                              test.answers[currentQuestion];
                          }}
                          rounded
                          fab
                          size="small">
                          <Icon class="default-icon" path={mdiDeleteOutline} />
                        </Button></td>
                    </tr>
                  {/each}
                {/if}

                <tr>
                  <td
                    contenteditable="true"
                    bind:innerHTML={newAnswer.answer} />
                  <td
                    style="width: 50px; text-align: right;"
                    contenteditable="true"
                    bind:innerHTML={newAnswer.score} />
                  <!-- <td style="width: 30px;"
                    ><div
                      class="d-flex justify-center"
                      style="padding-left: 12px;">
                      <Checkbox bind:checked={newAnswer.right} />
                    </div></td> -->
                  <td style="width: 20px; text-align: center;">
                    <Button
                      class="primary-color"
                      on:click={() => {
                        if (newAnswer.answer == "") {
                          alert("Вы не заполнили ответ");
                          return;
                        }

                        if (!Array.isArray(test.answers)) test.answers = [];
                        if (!Array.isArray(test.answers[currentQuestion]))
                          test.answers[currentQuestion] = [];

                        test.answers[currentQuestion] = insert(
                          test.answers[currentQuestion],
                          test.answers[currentQuestion].length,
                          newAnswer
                        );

                        newAnswer = {
                          answer: "",
                          score: 0,
                          right: false,
                        };
                      }}
                      rounded
                      fab
                      size="small">
                      <Icon class="default-icon" path={mdiPlus} />
                    </Button>
                  </td>
                </tr>
              </tbody>
            </table>
            <!-- {JSON.stringify(test.answers)} -->
          </div>
        {/if}
      </div>
    {:else}
      <p class="text-center">
        В Вашем тесте еще нет ни одного вопроса. Чтобы его добавить используйте
        кнопку 《➕ 》 слева.
      </p>
    {/if}
  </div>

  {#if "APP.debug" == "true"}
    <Card class="green">
      <h5 class="white-text text-center ma-2">Debug</h5>
      <p class="white-text ma-4" style="white-space: pre;">
        {debug}
      </p>
    </Card>
  {/if}

  {#if test != undefined && test.questions.length > 0}
    <div style="min-width: 100%;" class="d-flex justify-center mt-4">
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
  {/if}
</Card>
<!-- </Container> -->

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

<!-- </Page> -->
<style>
.wrapper {
  display: grid;
  width: 100%;
  /* height: 600px; */
  grid-template-columns: 100px 1fr;
  grid-template-rows: 60px 1fr;
  grid-template-areas:
    "questionsBar toolBar"
    "questionsBar editor"
    "questionsBar answersBar";
}

.questionsBar {
  /* max-height: 600px; */
  grid-area: questionsBar;
  border-right: 1px solid #fe275e;
  /* background-color: lightskyblue; */
  overflow-y: auto;
}

.questionsBar::-webkit-scrollbar-track {
  box-shadow: inset 0 0 4px rgba(24, 45, 167, 0.3);
  background-color: #f5f5f5;
}

.questionsBar::-webkit-scrollbar {
  width: 4px;
  background-color: #f5f5f5;
}

.questionsBar::-webkit-scrollbar-thumb {
  background-color: #fe275e;
  border-radius: 10px;
}

.toolBar {
  grid-area: toolBar;
  /* background-color: lightskyblue; */
  padding: 10px;
}

.editor {
  grid-area: editor;
  /* background-color: lightcoral; */
  display: block;
  padding: 10px;
}

.answersBar {
  grid-area: answersBar;
  /* background-color: aquamarine; */
  max-height: 600px;
  overflow-y: auto;
}

.answerTypeBlock {
  margin-left: 10px;
  margin-right: 10px;
  padding-bottom: 8px;
  background: #2054e3;
  border-radius: 8px;
}

.answerType {
  margin: 2px;
  padding: 10px;
  background: #2054e3;
  border-radius: 8px;
}

.answerType-active {
  margin: 2px;
  padding: 10px;
  background: #fe275e;
  border-radius: 8px;
}

.answers {
  margin-top: 8px;
  margin-left: 10px;
  margin-right: 10px;
}

.answersInputSettings {
  margin-top: 8px;
  margin-left: 10px;
  margin-right: 10px;
}

table {
  width: 100%;
  border-collapse: collapse;
}
/* Zebra striping */
tr:nth-of-type(odd) {
  background: #eee;
}
th {
  background: #2054e3;
  color: white;
  font-weight: bold;
}
td,
th {
  padding: 6px;
  border: 1px solid #ccc;
  text-align: left;
}
</style>
