<script>
import { Container, Card, Button, Icon } from "svelte-materialify/src";
import {
  mdiAt,
  mdiEye,
  mdiEyeOff,
  mdiLoading,
  mdiLogin,
  mdiFormTextboxPassword,
  mdiInformationVariant,
  mdiLogout,
  mdiDatabaseExport,
} from "@mdi/js";
import Page from "../components/Page.svelte";
import { user } from "./_store.js";
import { goto } from "@sapper/app";
</script>

<svelte:head>
  <title>Главная</title>
</svelte:head>

<Page>
  <Container class="text-center" style="max-width:1200px;">
    <Card class="pa-4" style="background: #fefefe">
      <h4>Школьные тесты онлайн</h4>

      <img
        src="/Home_testsing.png"
        alt="Школьные тесты онлайн"
        style="width: 80%;" />

      <p class="text-center">
        <Button
          class="primary-color"
          size="large"
          on:click={() => {
            goto("/tests");
          }}>
          <Icon path={mdiDatabaseExport} />
          <b style="font-size: 1.1em;"> Перейти к тестам</b>
        </Button>
      </p>

      <!-- {#if $user != undefined}
        <hr
          class="mt-1 mb-4"
          style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />
        <h5>Тесты для Вас ({$user.classNumber} класс)</h5>
      {/if}

      <hr
        class="mt-2 mb-4"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

      <h5>Новые тесты</h5> -->
    </Card>
  </Container>
</Page>

<style>
</style>
