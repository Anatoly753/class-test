<script>
  import PageTransition from "./../components/PageTransition.svelte";
  import {
    Container,
    Button,
    TextField,
    Checkbox,
    Icon,
    Card,
    Overlay,
    Alert,
  } from "svelte-materialify/src";
  import {
    mdiAt,
    mdiEye,
    mdiEyeOff,
    mdiLoading,
    mdiLogin,
    mdiFormTextboxPassword,
    mdiInformationVariant,
    mdiLogout,
  } from "@mdi/js";
  import { user } from "./_store.js";
  import { goto } from "@sapper/app";
  import { onMount } from "svelte";

  import axios from "axios";

  let name;
  user.subscribe((value) => {
    if (value) name = value.name;
  });

  // onMount(async () => {
  //   console.log($user.email + '!')
  // })
</script>

<svelte:head>
  <title>Профиль</title>
</svelte:head>

<PageTransition>
  <Container class="text-center" style="max-width:1200px;">
    <Card class="pa-4" style="background: #fefefe">
      <h4>Профиль</h4>

      <hr
        class="mt-1 mb-8"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

      <p>Добро пожаловать, {name}!</p>

      <Button
        class="secondary-color"
        on:click={() => {
          $user = undefined;
          goto('/');
        }}>
        <Icon path={mdiLogout} />
        Выйти из аккаунта
      </Button>

      <hr
        class="mt-4 mb-4"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

      <h5>Статистика</h5>

      <h5>Мои последние результаты</h5>
    </Card>
  </Container>
</PageTransition>
