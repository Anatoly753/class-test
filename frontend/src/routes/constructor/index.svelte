<!--<script>
  import { Container, Button, Card, Icon } from "svelte-materialify/src";
  import { mdiFormatListCheckbox, mdiPlaylistPlus } from "@mdi/js";
  import Page from "../../components/Page.svelte";
  import { user } from "../_store.js";
  import { goto } from "@sapper/app";

  function set() {
    $user = { id: 123, name: "Name" };
  }

  function clear() {
    $user = undefined;
  }
</script><svelte:head>
  <title>Конструктор тестов</title>
</svelte:head>

<Page authenticationRequired>
  <Container class="text-center" style="max-width:1200px;">
    <Card class="pa-4" style="background: #fefefe">
      <h4>Конструктор тестов</h4>

      <hr
        class="mt-1 mb-8"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

      <p>
        <Button
          class="primary-color pa-3 pl-9 pr-9"
          on:click={() => {
            goto(`/constructor/list`, { scroll: true });
          }}
          rounded
          size="large">
          <Icon path={mdiFormatListCheckbox} />
          Мои тесты
        </Button>
      </p>

      <p>
        <Button
          class="primary-color pa-3 pl-8 pr-8"
          on:click={() => {
            goto(`/constructor/create`, { scroll: true });
          }}
          rounded
          size="large">
          <Icon path={mdiPlaylistPlus} />
          Новый тест
        </Button>
      </p>
    </Card>
  </Container>
</Page><style>
</style>-->
