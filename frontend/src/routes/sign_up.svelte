<script>
  import Page from "./../components/Page.svelte";
  import {
    Container,
    Button,
    TextField,
    Checkbox,
    Icon,
    Card,
    Overlay,
    Alert,
    Slider,
  } from "svelte-materialify/src";
  import {
    mdiAt,
    mdiEye,
    mdiEyeOff,
    mdiLoading,
    mdiAccountPlus,
    mdiFormTextboxPassword,
    mdiInformationVariant,
    mdiAccountSupervisorCircle,
    mdiCardAccountDetailsOutline,
    mdiAccount,
    mdiPlus,
    mdiMinus,
    mdiAlertOutline,
  } from "@mdi/js";

  import { user } from "./_store.js";
  import { goto } from "@sapper/app";
  import { fade } from "svelte/transition";

  import axios from "axios";

  let requestError = false,
    requestErrorMessage = "";

  let patronymicOverlay = false,
    passwordOverlay = false;

  let name = "",
    surname = "",
    patronymic = "",
    email = "",
    password = "",
    repeatPassword = "",
    classNumber = 1;

  let nameError = false,
    surnameError = false,
    patronymicError = false,
    emailError = false,
    passwordError = false,
    repeatPasswordError = false;

  let termsOfUse = false;
  let checkboxWarning = false;
  $: if (termsOfUse) checkboxWarning = false;

  let personalData = false;
  let personalDataWarning = false;
  $: if (personalData) personalDataWarning = false;

  //FIXME: rules in other file, then import

  const nameRules = [
    (v) => !!v || "Вам необходимо заполнить это поле",
    (v) => v.length <= 30 || "Максимальная длина этого поля - 30 символов",
    (v) => {
      const pattern = /^(([a-zA-Z' -]{1,30})|([а-яА-ЯЁёІіЇїҐґЄє' -]{1,30}))$/;
      return pattern.test(v) || "Вы ввели неверное имя";
    },
  ];

  const surnameRules = [
    (v) => !!v || "Вам необходимо заполнить это поле",
    (v) => v.length <= 30 || "Максимальная длина этого поля - 30 символов",
    (v) => {
      const pattern = /^(([a-zA-Z' -]{1,30})|([а-яА-ЯЁёІіЇїҐґЄє' -]{1,30}))$/;
      return pattern.test(v) || "Вы ввели неверную фамилию";
    },
  ];

  const patronymicRules = [
    (v) => v.length <= 30 || "Максимальная длина этого поля - 30 символов",
    (v) => {
      const pattern = /^(([a-zA-Z' -]{1,30})|([а-яА-ЯЁёІіЇїҐґЄє' -]{1,30}))$/;
      if (v.length != 0) return pattern.test(v) || "Вы ввели неверное отчество";
    },
  ];

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

  let showPassword = false,
    showRepeatPassword = false;

  let disabled = false;

  function checkRules(value, rules) {
    let errors = rules
      .map((r) => r(value))
      .filter((r) => typeof r === "string");

    if (errors.length) return true;
    return false;
  }

  async function sign_up() {
    disabled = true;

    requestError = false;
    requestErrorMessage = "";

    name = name.trim();
    surname = surname.trim();
    patronymic = patronymic.trim();
    email = email.trim();

    nameError = checkRules(name, nameRules);
    surnameError = checkRules(surname, surnameRules);
    patronymicError = checkRules(patronymic, patronymicRules);
    emailError = checkRules(email, emailRules);
    passwordError = checkRules(password, passwordRules);
    repeatPasswordError = checkRules(repeatPassword, passwordRules);

    if (!termsOfUse) checkboxWarning = true;

    if (!personalData) personalDataWarning = true;

    if (
      emailError ||
      passwordError ||
      nameError ||
      surnameError ||
      patronymicError ||
      !termsOfUse ||
      !personalData
    ) {
      disabled = false;
      return;
    }

    axios({
      method: "post",
      url: "APP.API/users",
      headers: {
        "X-Requested-With": "XMLHttpRequest",
        "Content-Type": "application/json",
      },
      data: {
        user: {
          email: email,
          password: password,
          name: name,
          surname: surname,
          patronymic: patronymic == "" ? null : patronymic,
          classNumber: classNumber,
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
  <title>Регистрация</title>
</svelte:head>

<Page>
  <Container class="text-center" style="max-width:600px;">
    <Card class="pa-4" style="background: #fefefe">
      <h4>Регистрация</h4>
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
        bind:value={name}
        counter={30}
        rules={nameRules}
        error={nameError}
        class="mb-2"
        outlined
        rounded>
        <div slot="prepend">
          <Icon path={mdiAccount} />
        </div>
        Имя
      </TextField>

      <TextField
        bind:value={surname}
        counter={30}
        rules={surnameRules}
        error={surnameError}
        class="mb-2"
        outlined
        rounded>
        <div slot="prepend">
          <Icon path={mdiAccountSupervisorCircle} />
        </div>
        Фамилия
      </TextField>

      <TextField
        bind:value={patronymic}
        counter={30}
        rules={patronymicRules}
        error={patronymicError}
        class="mb-2"
        outlined
        rounded
        type="text">
        <div slot="prepend">
          <Icon path={mdiCardAccountDetailsOutline} />
        </div>
        Отчество
        <div slot="append">
          <Button
            icon
            class="primary-text"
            on:click={() => {
              patronymicOverlay = true;
            }}>
            <Icon class="default-icon" path={mdiInformationVariant} />
          </Button>
        </div>
      </TextField>

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
        type={showPassword ? 'text' : 'password'}>
        <div slot="prepend">
          <Icon path={mdiFormTextboxPassword} />
        </div>
        Пароль
        <div slot="append">
          <Button
            icon
            class="primary-text"
            on:click={() => {
              showPassword = !showPassword;
            }}>
            <Icon
              class="default-icon"
              path={showPassword ? mdiEyeOff : mdiEye} />
          </Button>
          <Button
            icon
            class="primary-text"
            on:click={() => {
              passwordOverlay = true;
            }}>
            <Icon class="default-icon" path={mdiInformationVariant} />
          </Button>
        </div>
      </TextField>

      <TextField
        bind:value={repeatPassword}
        counter={255}
        rules={passwordRules}
        error={repeatPasswordError}
        outlined
        rounded
        type={showRepeatPassword ? 'text' : 'password'}>
        <div slot="prepend">
          <Icon path={mdiFormTextboxPassword} />
        </div>
        Повторите пароль
        <div slot="append">
          <Button
            icon
            class="primary-text"
            on:click={() => {
              showRepeatPassword = !showRepeatPassword;
            }}>
            <Icon
              class="default-icon"
              path={showRepeatPassword ? mdiEyeOff : mdiEye} />
          </Button>
        </div>
      </TextField>

      <p class="pb-4">В каком классе Вы учитесь?</p>
      <Slider
        thumb
        persistentThumb
        min={1}
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

      <div class="pb-6 d-flex justify-center">
        <Checkbox
          bind:checked={personalData}
          class={personalDataWarning ? 'red-text' : 'primary-text'}>
          <b class={personalDataWarning ? 'red-text' : 'primary-text'}>
            Я даю согласие на обработку персональных данных
          </b>
        </Checkbox>
      </div>

      <Button
        class="primary-color"
        on:click={sign_up}
        {disabled}
        rounded
        size="large">
        {#if disabled == true}
          <Icon spin path={mdiLoading} />
          Загрузка...
        {:else}
          <Icon path={mdiAccountPlus} />
          Регистрация
        {/if}
      </Button>
    </Card>
  </Container>

  <Overlay
    active={patronymicOverlay}
    on:click={() => {
      patronymicOverlay = false;
    }}>
    <Alert
      class="primary-color ma-2"
      dismissible
      on:dismiss={() => {
        patronymicOverlay = false;
      }}>
      <h5 class="mb-4">Подсказка для поля <b>«отчество»</b></h5>
      Оставьте это поле
      <b>пустым,</b>
      если у Вас
      <b>нет</b>
      отчества
    </Alert>
  </Overlay>

  <Overlay
    active={passwordOverlay}
    on:click={() => {
      passwordOverlay = false;
    }}>
    <Alert
      class="primary-color ma-2"
      dismissible
      on:dismiss={() => {
        passwordOverlay = false;
      }}>
      <h5 class="mb-4">Пароль должен соотвествовать условиям:</h5>
      <ul>
        <li>Длина пароля <b>не меньше</b> 8 и <b>не больше</b> 255 символов</li>
        <li><b>Минимум одна</b> буква и цифра</li>
        <li>Пароль <b>может</b> содержать специальные символы</li>
      </ul>
    </Alert>
  </Overlay>
</Page>
