<script>
  import PageTransition from "./../components/PageTransition.svelte";
  import { mdiHome, mdiRefresh } from "@mdi/js";
  import { Container, Button, Icon, Card } from "svelte-materialify/src";
  import { goto } from "@sapper/app";

  export let status;
  export let error;

  const dev = process.env.NODE_ENV === "development";
</script>

<svelte:head>
  <title>{status}</title>
</svelte:head>

<PageTransition>
  <Container class="text-center" style="max-width:700px;">
    <Card class="pa-4" style="background: #fefefe">
      <h4>Ошибка {status}</h4>
      <hr
        class="mb-8"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

      <p>При выполнении вашего запроса возникла ошибка:</p>
      <p class="text-center">{error.message}</p>

      <Button
        class="primary-color"
        on:click={() => goto('/')}
        rounded
        size="large">
        <Icon path={mdiHome} />
        Перейти на главную
      </Button>

      <div class="d-md-none mb-4" />

      <Button
        class="primary-color"
        on:click={() => location.reload()}
        rounded
        size="large">
        <Icon path={mdiRefresh} />
        Обновить страницу
      </Button>

      {#if dev && error.stack}
        <pre>{error.stack}</pre>
      {/if}

      {#if status == 404}
        <img
          class="mt-10 rounded-t-lg rounded-b-lg"
          src="404-min.jpeg"
          alt="" />
      {/if}
    </Card>
  </Container>
</PageTransition>

<style>
  img {
    width: 95%;
  }
</style>
