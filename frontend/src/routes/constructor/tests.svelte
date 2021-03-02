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
    Dialog,
  } from "svelte-materialify/src";
  import ExpansionPanels, {
    ExpansionPanel,
  } from "svelte-materialify/src/components/ExpansionPanels";
  import {
    mdiSchoolOutline,
    mdiClockOutline,
    mdiPlus,
    mdiMinus,
    mdiRefresh,
    mdiChevronDown,
    mdiLoading,
    mdiFileSearchOutline,
    mdiPlaylistEdit,
    mdiBookOpenOutline,
    mdiDeleteAlertOutline,
    mdiFrequentlyAskedQuestions,
    mdiProgressWrench,
  } from "@mdi/js";
  import ContentLoader from "svelte-content-loader";
  import { user } from "../_store.js";
  import { goto } from "@sapper/app";
  import { fade, slide } from "svelte/transition";
  import { onMount } from "svelte";
  import axios from "axios";
  import formatDistanceToNow from "date-fns/formatDistanceToNow";
  import format from "date-fns/format";
  import { id, ru } from "date-fns/locale";

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

  let subjectItems = [
    { name: "Все", value: "Все" },
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

  let searchOptionsActive = false;

  let title = "";
  let subject = "Все";
  let classNumber = 0;
  let sort = "От новых к старым";

  let sortItems = [
    { name: "От новых к старым", value: "От новых к старым" },
    { name: "От старых к новым", value: "От старых к новым" },
  ];

  let openedTests = [];

  let loading = true;
  let tests = [];

  let deleteTestId;

  let deleteDialogActive = false;
  let deleteButtonDisabled = true;
  let deleteLoading = false;
  let deleteTimerId, deleteTimeoutId;
  let deleteTimer = 5;

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
          subject:
            subject == "Все"
              ? null
              : subjectItems.find((item) => item.name === subject).key,
          classNumber: classNumber == 0 ? null : classNumber,
          author: { id: $user.id },
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

  async function delete_test() {
    deleteButtonDisabled = true;
    deleteLoading = true;

    axios({
      method: "delete",
      url: `APP.API/tests/${deleteTestId}`,
      headers: {
        "X-Requested-With": "XMLHttpRequest",
        "Content-Type": "application/json",
        Authorization: `Token ${$user.token}`,
      },
      withCredentials: true,
    })
      .then((res) => {
        searchTests();
      })
      .catch((error) => {
        console.log(error);
        alert(error.message);
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

  const titleRules = [
    (v) => (v.length >= 3 && length <= 150) || "Введите минимум 3 символа",
    (v) => v.length <= 150 || "Максимум 150 символов",
  ];

  function onChange() {
    if (title.length >= 3 && title.length <= 150) searchTests();
  }

  export let params;
</script>

<svelte:head>
  <title>Мои тесты</title>
</svelte:head>

<Page authenticationRequired>
  <Container style="max-width:1000px;">
    <Card class="pa-4">
      <h4 class="text-center">Мои тесты</h4>
      <hr
        class="mt-1 mb-4"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

      <div class="d-flex justify-center mt-4 mb-4">
        <Card style="max-width:900px; width: 100%;">
          <CardTitle>
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
                counter="150"
                maxlength="150"
                rules={titleRules}
                class="mt-2"
                on:input={onChange}>
                <div slot="prepend">
                  <Icon path={mdiFileSearchOutline} />
                </div>
                Поиск теста
              </TextField>

              <div style="max-width: 250px; margin: 0 auto;">
                <Select outlined items={subjectItems} bind:value={subject}>
                  Поиск по предмету
                </Select>
              </div>

              <p class="text-center" style="height: 35px;">
                <b>Класс: {classNumber == 0 ? 'Все (1-11)' : classNumber} </b>
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
          <!-- <ExpansionPanels
            multiple
            popout
            bind:value={openedTests}
            style="max-width: 900px; width: 100%;"> -->
          <List class="elevation-2" style="max-width: 900px; width: 100%;">
            {#each tests || [] as test}
              <!-- <ExpansionPanel> -->
              <!-- <span slot="header"> -->
              <ListItem
                on:click={() => {
                  goto(`/constructor/editor/${test.id}/home`, { scroll: true });
                }}>
                <h6>{test.title}</h6>
                <Icon path={mdiBookOpenOutline} class="default-icon" />
                {subjects[test.subject]}
                <Icon path={mdiSchoolOutline} class="default-icon" />
                {test.classNumber}
                класс
                <Icon path={mdiClockOutline} class="default-icon" />
                Добавлен:
                {formatDistanceToNow(new Date(test.createdAt.split('[')[0]), {
                  addSuffix: true,
                  locale: ru,
                })}
              </ListItem>
              <!-- </span> -->

              <!-- </ExpansionPanel> -->
            {/each}
            <!-- </ExpansionPanels> -->
          </List>
        {:else}Пока здесь ничего нет{/if}
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
              clearTimeout(deleteTimeoutId);
              deleteTestId = undefined;
              deleteButtonDisabled = true;
              deleteLoading = false;
              deleteTimer = 5;
              deleteDialogActive = false;
            }}
            rounded
            size="large"
            class="primary-color white-text">
            Отмена
          </Button>
        </div>
      </CardActions>
    </Card>
  </Dialog>
</Page>

<style>
  .slider {
    width: 80%;
    margin: 0 auto;
  }

  @media (max-width: 800px) {
    .slider {
      width: 100%;
    }
  }
</style>
