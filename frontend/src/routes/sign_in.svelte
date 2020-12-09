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
    mdiAlertOutline,
  } from "@mdi/js";

  import { user } from "./_store.js";
  import { goto } from "@sapper/app";
  import { fade } from "svelte/transition";

  import axios from "axios";

  let requestError = false,
    requestErrorMessage = "";

  let overlayActive = false;

  let email = "",
    password = "";
  let emailError = false,
    passwordError = false;
  let termsOfUse = false;
  let checkboxWarning = false;
  $: if (termsOfUse) checkboxWarning = false;

  const emailRules = [
    (v) => !!v || "Вам необходимо заполнить это поле",
    (v) => v.length <= 255 || "Максимальная длина этого поля - 255 символов",
    (v) => {
      const pattern = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
      return pattern.test(v) || "Вы ввели неверный e-mail";
    },
  ];

  const passwordRules = [
    (v) => !!v || "Вам необходимо заполнить это поле",
    (v) => v.length >= 8 || "Минимальная длина пароля - 8 символов",
    (v) => v.length <= 255 || "Максимальная длина пароля - 255 символов",
    (v) => {
      const pattern = /^(?=.*\d)(?=.*[a-zA-Z]).{8,255}$/;
      return pattern.test(v) || "Вы ввели несоответствующий условиям пароль";
    },
  ];

  let show = false;
  let disabled = false;

  function checkRules(value, rules) {
    let errors = rules
      .map((r) => r(value))
      .filter((r) => typeof r === "string");

    if (errors.length) return true;
    return false;
  }

  async function sign_in() {
    disabled = true;

    requestError = false;
    requestErrorMessage = "";

    emailError = checkRules(email, emailRules);
    passwordError = checkRules(password, passwordRules);

    if (!termsOfUse) checkboxWarning = true;

    if (emailError || passwordError || !termsOfUse) {
      disabled = false;
      return;
    }

    axios({
      method: "post",
      url: "APP.API/users/login",
      headers: {
        "X-Requested-With": "XMLHttpRequest",
        "Content-Type": "application/json",
      },
      data: {
        user: {
          email: email,
          password: password,
        },
      },
      withCredentials: true,
    })
      .then((response) => {
        $user = response.data.user;
        goto("/profile");
      })
      .catch((error) => {
        console.log(error);
        if (error.response) {
          requestErrorMessage = JSON.stringify(error.response.data.errors);
          requestError = !requestError;
        } else {
          requestErrorMessage = error.message;
          requestError = !requestError;
        }
      })
      .finally(() => {
        disabled = false;
      });
  }
</script>

<svelte:head>
  <title>Вход</title>
</svelte:head>

<PageTransition>
  <Container class="text-center" style="max-width:600px;">
    <Card class="pa-4" style="background: #fefefe">
      <h4>Вход</h4>
      <hr
        class="mt-1 mb-8"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

      {#if requestError}
        <p transition:fade>
          <Alert class="error-text" text dense>
            <h6 class="mb-2">
              <Icon class="white-text" path={mdiAlertOutline} />
              При выполнении действия произошла ошибка
            </h6>
            <p class="text-left">{requestErrorMessage}</p>
          </Alert>
        </p>
      {/if}

      <TextField
        bind:value={email}
        counter={255}
        rules={emailRules}
        error={emailError}
        class="mb-2"
        outlined
        rounded>
        <div slot="prepend">
          <Icon path={mdiAt} />
        </div>
        E-mail
      </TextField>

      <TextField
        bind:value={password}
        counter={255}
        rules={passwordRules}
        error={passwordError}
        class="mb-2"
        outlined
        rounded
        type={show ? 'text' : 'password'}>
        <div slot="prepend">
          <Icon path={mdiFormTextboxPassword} />
        </div>
        Пароль
        <div slot="append">
          <Button
            icon
            class="primary-text"
            on:click={() => {
              show = !show;
            }}>
            <Icon class="default-icon" path={show ? mdiEyeOff : mdiEye} />
          </Button>

          <Button
            icon
            class="primary-text"
            on:click={() => {
              overlayActive = !overlayActive;
            }}>
            <Icon class="default-icon" path={mdiInformationVariant} />
          </Button>
        </div>
      </TextField>

      <div class="pb-6 d-flex justify-center">
        <Checkbox
          bind:checked={termsOfUse}
          class={checkboxWarning ? 'red-text' : 'primary-text'}>
          <b class={checkboxWarning ? 'red-text' : 'primary-text'}>
            Я согласен с
            <a href="/terms_of_use" target="_blank">условиями использования</a>
          </b>
        </Checkbox>
      </div>

      <Button
        class="primary-color"
        on:click={sign_in}
        {disabled}
        rounded
        size="large">
        {#if disabled == true}
          <Icon spin path={mdiLoading} />
          Загрузка...
        {:else}
          <Icon path={mdiLogin} />
          Войти
        {/if}
      </Button>
    </Card>
  </Container>

  <!-- TODO: Overlay Capcha -->

  <Overlay
    active={overlayActive}
    on:click={() => {
      overlayActive = false;
    }}>
    <Alert
      class="primary-color ma-2"
      dismissible
      on:dismiss={() => {
        overlayActive = false;
      }}>
      <h5 class="mb-4">Пароль должен соотвествовать условиям:</h5>
      <ul>
        <li>Длина пароля не меньше 8 и не больше 255 символов</li>
        <li>Минимум одна буква и цифра</li>
        <li>Пароль может содержать специальные символы</li>
      </ul>
    </Alert>
  </Overlay>
</PageTransition>
