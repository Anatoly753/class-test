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
  Window,
  WindowItem,
  ProgressLinear,
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
  mdiNumeric,
  mdiText,
  mdiArrowLeftBold,
  mdiArrowRightBold,
  mdiMessageArrowLeftOutline,
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
import TextField from "svelte-materialify/src/components/TextField";
import Checkbox from "svelte-materialify/src/components/Checkbox";
import Radio from "svelte-materialify/src/components/Radio";
import Textarea from "svelte-materialify/src/components/Textarea";

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

let window,
  currentQuestion = 0,
  studentAnswers = [],
  questions = [];

let loading = true;
let test;

let answers = [];

let showResult = false;
let result;

////////////
// showResult = true;
// result = { score: 10 };

$: if (
  test?.settings?.questions?.[currentQuestion]?.answerType ==
    "multipleSelect" &&
  answers[currentQuestion] == null
) {
  answers[currentQuestion] = [];
}

$: if (
  test?.settings?.questions?.[currentQuestion]?.answerType == "range" &&
  answers[currentQuestion] == null
) {
  answers[currentQuestion] = [1, 2];
}

$: debug = JSON.stringify(test, null, 2);
$: answers_debug = JSON.stringify(answers, null, 2);

onMount(async () => {
  findTest();

  // test.questions.forEach((question) => answers.push({ answer: undefuned }));
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
      console.log(test);
      test.questions =
        typeof test?.questions == "string" && test?.questions.length > 0
          ? JSON.parse(test.questions)
          : [];
      console.log(test.questions);
      test.answers =
        typeof test?.answers == "string" && test?.answers.length > 0
          ? JSON.parse(JSON.parse(test.answers))
          : [];
      console.log(test.answers);
      test.settings =
        typeof test?.settings == "string" && test?.settings.length > 0
          ? JSON.parse(test.settings)
          : {};

      if (test?.settings?.questions != undefined) {
        test.settings.questions.forEach((q, i) => {
          if (q?.answerParams?.random_answers == true) shuffle(test.answers[i]);
        });
      }
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

async function sendTestForVerification() {
  answers.forEach((el, i) => {
    if (
      Array.isArray(el) &&
      test.settings.questions[i].answerType == "multipleSelect"
    ) {
      if (el.length > 0) {
        el.forEach((a, j) => {
          answers[i][j] = { answer: test.answers[i][a].answer };
          console.log(test.answers[i][a].answer);
        });
      } else {
        answers[i] = [{ answer: "" }];
      }
    } else if (test.settings.questions[i].answerType == "slider") {
      answers[i] = [{ answer: "" + el }];
    } else if (
      Array.isArray(el) &&
      test.settings.questions[i].answerType == "range"
    ) {
      answers[i] = [{ answer: `${el[0]}-${el[1]}` }];
    } else if (
      test.settings.questions[i].answerType == "select" &&
      typeof el == "number"
    ) {
      answers[i] = [{ answer: test.answers[i][el].answer }];
    } else {
      answers[i] = [{ answer: el }];
    }
  });

  axios({
    method: "post",
    url: `APP.API/tests/check/${params.id}`,
    headers: {
      "X-Requested-With": "XMLHttpRequest",
      "Content-Type": "application/json",
      Authorization: `Token ${$user.token}`,
    },
    data: JSON.stringify(answers),
    withCredentials: true,
  })
    .then((res) => {
      result = res.data;
      console.log(result);
      //goto(`/constructor/editor/${test.id}/home`, { scroll: true });
    })
    .catch((error) => {
      console.log(error);
      // error(error.message);
      if (error.response) {
        // requestErrorMessage = JSON.stringify(error.response.data.errors);
        // requestError = !requestError;
      } else {
        // requestErrorMessage = error.message;
        // requestError = !requestError;
      }
    })
    .finally(() => {
      //disabled = false;
      showResult = true;
    });
}

function transformAnswersForDropDown(q_answers) {
  let items = [];
  q_answers.forEach((a) => items.push({ name: a.answer, value: a.answer }));
  return items;
}

const insert = (arr, index, newItem) => [
  ...arr.slice(0, index),
  newItem,
  ...arr.slice(index),
];

function shuffle(array) {
  for (let i = array.length - 1; i > 0; i--) {
    let j = Math.floor(Math.random() * (i + 1));
    [array[i], array[j]] = [array[j], array[i]];
  }
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
  <Container style="max-width:1200px;">
    <Card class="pa-4">
      {#if test != undefined && test.questions.length > 0}
        {#if showResult == false}
          <h4 class="text-center">{test.title}</h4>
          <hr
            class="mt-1 mb-4"
            style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

          <ProgressLinear
            class="mb-4"
            height="20px"
            rounded
            backgroundOpacity={0.4}
            value={Math.round((currentQuestion / test.questions.length) * 100)}>
            <div style="height:20px;">
              <p
                class="white-text"
                style="line-height: 20px; display: table-cell; vertical-align: middle;">
                {currentQuestion}
                из
                {test.questions.length}
                -
                {Math.round((currentQuestion / test.questions.length) * 100)}%
              </p>
            </div>
          </ProgressLinear>

          <Window
            bind:this={window}
            bind:value={currentQuestion}
            continuous={false}
            vertical>
            <!-- {#if test != undefined && test.questions.length > 0} -->
            {#each test.questions || [] as _, i}
              <WindowItem>
                <div class="slide pa-4">
                  {@html test.questions[i]}

                  <div class="answer mt-8">
                    <hr
                      class="mt-1 mb-4"
                      style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />
                    <h5 class="mb-2">Ответ:</h5>
                    {#if test?.settings?.questions?.[currentQuestion] != undefined && test?.answers?.[currentQuestion] != undefined}
                      <!-- {#each test.settings.questions[currentQuestion] as questionSettings} -->
                      {#if test.settings.questions[currentQuestion].answerType == "number"}
                        <!-- if integer OR demical -->
                        <TextField
                          class="mb-2"
                          outlined
                          type="number"
                          step="1"
                          pattern="\d+"
                          bind:value={answers[currentQuestion]}>
                          <div slot="prepend">
                            <Icon path={mdiNumeric} />
                          </div>
                          Введите ответ
                        </TextField>
                      {:else if test.settings.questions[currentQuestion].answerType == "text"}
                        {#if test.settings.questions[currentQuestion].answerParams.multiple_lines == true}
                          <Textarea
                            bind:value={answers[currentQuestion]}
                            outlined
                            counter={255}
                            autogrow
                            rows={5}>Введите ответ (несколько строк)</Textarea>
                        {:else}
                          <TextField
                            class="mb-2"
                            outlined
                            type="text"
                            bind:value={answers[currentQuestion]}>
                            <div slot="prepend">
                              <Icon path={mdiText} />
                            </div>
                            Введите ответ
                          </TextField>
                        {/if}
                      {:else if test.settings.questions[currentQuestion].answerType == "select"}
                        {#each test.answers[currentQuestion] || {} as answerOption, i}
                          <Radio bind:group={answers[currentQuestion]} value={i}
                            >{answerOption.answer}</Radio>
                        {/each}
                      {:else if test.settings.questions[currentQuestion].answerType == "multipleSelect"}
                        {#each test.answers[currentQuestion] || {} as answerOption, i}
                          <!-- function default value [] -->
                          <Checkbox
                            bind:group={answers[currentQuestion]}
                            value={i}>{answerOption.answer}</Checkbox>
                        {/each}
                      {:else if test.settings.questions[currentQuestion].answerType == "dropDownList"}
                        <div style="width: 350px; min-height: 150px;">
                          <Select
                            outlined
                            items={transformAnswersForDropDown(
                              test.answers[currentQuestion]
                            )}
                            bind:value={answers[currentQuestion]}>Ответ
                          </Select>
                        </div>
                        <div style="height: 50px;" />
                      {:else if test.settings.questions[currentQuestion].answerType == "slider"}
                        <p class="ml-4 mr-4 mt-8">
                          <Slider
                            thumb
                            persistentThumb
                            min={parseInt(
                              test.settings.questions[currentQuestion]
                                .answerParams.min
                            ) || 1}
                            max={parseInt(
                              test.settings.questions[currentQuestion]
                                .answerParams.max
                            ) || 5}
                            step={parseInt(
                              test.settings.questions[currentQuestion]
                                .answerParams.step
                            ) || 1}
                            bind:value={answers[currentQuestion]}
                            inverseLabel
                            color="primary">
                            <span slot="prepend-outer">
                              <Button
                                icon
                                class="primary-text slider-button"
                                on:click={() => {
                                  let step =
                                    parseInt(
                                      test.settings.questions[currentQuestion]
                                        .answerParams.step
                                    ) || 1;
                                  answers[currentQuestion] -= step;
                                }}>
                                <Icon path={mdiMinus} class="default-icon" />
                              </Button>
                            </span>
                            <span slot="append-outer" class="ml-0">
                              <Button
                                icon
                                class="primary-text slider-button"
                                on:click={() => {
                                  let step =
                                    parseInt(
                                      test.settings.questions[currentQuestion]
                                        .answerParams.step
                                    ) || 1;
                                  answers[currentQuestion] += step;
                                }}>
                                <Icon path={mdiPlus} class="default-icon" />
                              </Button>
                            </span>
                          </Slider>
                        </p>
                      {:else if test.settings.questions[currentQuestion].answerType == "range"}
                        <p class="ml-4 mr-4 mt-8">
                          <Slider
                            thumb
                            persistentThumb
                            min={parseInt(
                              test.settings.questions[currentQuestion]
                                .answerParams.min
                            ) || 1}
                            max={parseInt(
                              test.settings.questions[currentQuestion]
                                .answerParams.max
                            ) || 5}
                            step={parseInt(
                              test.settings.questions[currentQuestion]
                                .answerParams.step
                            ) || 1}
                            bind:value={answers[currentQuestion]}
                            inverseLabel
                            color="primary" />
                        </p>
                      {/if}
                    {/if}
                  </div>
                </div>
              </WindowItem>
            {/each}
            <!-- {/if} -->

            <WindowItem>
              <div class="slide rounded" style="background: #5273ad">
                <h4 class="text-center white-text">Вопросы закончились</h4>
                <p class="text-center">
                  <img src="finish.png" width="50%" alt="Finish" />
                </p>
                <p class="text-center">
                  <!-- <Button
                  class="primary-color"
                  size="large"
                  on:click={() => {
                    alert(123);
                  }}
                  ><h6>
                    <Icon path={mdiMessageArrowLeftOutline} /> Вернуться к пропущенным
                  </h6></Button> -->
                  <Button
                    class="primary-color"
                    on:click={() => {
                      sendTestForVerification();
                    }}>
                    <Icon path={mdiFlagCheckered} /> Завершить и перейти к результатам
                  </Button>
                </p>
              </div>
            </WindowItem>
          </Window>

          <br />

          <!-- <br /> -->

          <div class="d-flex justify-space-between">
            <Button
              class="primary-color"
              disabled={currentQuestion == 0}
              on:click={() => {
                window.previous();
              }}><Icon path={mdiArrowLeftBold} /> Назад</Button>
            <Button
              class="primary-color"
              disabled={currentQuestion >= test.questions.length}
              on:click={() => {
                window.next();
              }}>Дальше <Icon path={mdiArrowRightBold} /></Button>
          </div>

          <!-- <div class="d-flex justify-center"> -->
          <!-- <p>
          <Icon path={mdiSchoolOutline} class="default-icon" />
          {test.classNumber}
          класс
        </p>
        <p>
          <Icon path={mdiClockOutline} class="default-icon" />
          Добавлен:
          {formatDistanceToNow(new Date(test.createdAt.split('[')[0]), {
            addSuffix: true,
            locale: ru,
          })}
        </p> -->

          <!-- <Button
          class="primary-color"
          on:click={() => {
            goto(`/test/solve/${params.id}`, { scroll: true });
          }}
          rounded
          size="large">
          <Icon path={mdiFlagCheckered} />
          Пройти тест
        </Button> -->
          <!-- </div> -->
        {:else}
          <div class="results" style="width: 100%;">
            <h3 class="text-center">Ваш результат</h3>
            <hr
              class="mt-1 mb-4"
              style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />
            <div class="image-container">
              <img
                src="/results.jpg"
                alt="Result background"
                style="display: block; margin: 0px auto; width: 96%; border-radius: 10px;" />
              <h4>
                {result.score} из {result.maxScore} <br /> баллов <br /><br />
                Оценка - {(result.score / result.maxScore) * 100 >= 87
                  ? 5
                  : (result.score / result.maxScore) * 100 >= 66
                  ? 4
                  : (result.score / result.maxScore) * 100 >= 42
                  ? 3
                  : 2}
              </h4>
            </div>
          </div>
        {/if}
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

{#if "APP.debug" == "true"}
  <Container style="max-width: 1500px;">
    <Card class="green">
      <h5 class="white-text text-center ma-2">Answers Debug</h5>
      <p class="white-text ma-4" style="white-space: pre;">
        {answers_debug}
      </p>
    </Card>

    <Card class="green">
      <h5 class="white-text text-center ma-2">Debug</h5>
      <p class="white-text ma-4" style="white-space: pre;">
        {debug}
      </p>
    </Card>
  </Container>
{/if}

<style>
.slide {
  /* height: 600px; */
  overflow-y: auto;
  /* display: flex; */
  /* align-items: center; */
  /* justify-content: center; */
  /* color: #fff; */
}

.image-container {
  position: relative;
  text-align: center;
  color: red;
}

.results h4 {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 2.5em;
}
</style>
