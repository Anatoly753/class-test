<script>
  import { Container, Button } from "svelte-materialify/src";
  import PageTransition from "../components/PageTransition.svelte";
  import { user } from "./_store.js";

  function set() {
    $user = { id: 123, name: "Name" };
  }

  function clear() {
    $user = undefined;
  }
</script>

<svelte:head>
  <title>ClassTest - Главная</title>
</svelte:head>
<PageTransition>
  <Container class="text-center">

    <img
      src="//raw.githubusercontent.com/TheComputerM/svelte-materialify/master/logo/logo-192.png"
      alt="svelte-materialify"
    />
    <h2 class="mb-3">Home page</h2>

    <Button class="primary-color" on:click="{set}">Set</Button>
    <Button class="secondary-color" on:click="{clear}">Clear</Button>
  </Container>
</PageTransition>

<style>

</style>
