<script>
  import Page from "../Page.svelte";
  import {
    Container,
    Button,
    TextField,
    Icon,
    Card,
    Overlay,
    Alert,
    Slider,
    Divider,
  } from "svelte-materialify/src";
  import {
    mdiAt,
    mdiLoading,
    mdiInformationVariant,
    mdiAccountSupervisorCircle,
    mdiCardAccountDetailsOutline,
    mdiAccount,
    mdiPlus,
    mdiMinus,
    mdiAlertOutline,
    mdiAccountEdit,
    mdiChevronDown,
    mdiFormTextboxPassword,
    mdiEyeOff,
    mdiEye,
    mdiLock,
    mdiKeyChainVariant,
  } from "@mdi/js";

  import { user } from "../../routes/_store.js";
  import { goto } from "@sapper/app";
  import { fade, slide } from "svelte/transition";

  import axios from "axios";
  import CardTitle from "svelte-materialify/src/components/Card/CardTitle.svelte";
  import CardSubtitle from "svelte-materialify/src/components/Card/CardSubtitle.svelte";
  import CardActions from "svelte-materialify/src/components/Card/CardActions.svelte";

  let requestError = false,
    requestErrorMessage = "";

  let patronymicOverlay = false,
    passwordOverlay = false;

  let name = "",
    surname = "",
    patronymic = "",
    email = "",
    classNumber = 1,
    currentPassword = "",
    password = "",
    repeatPassword = "";

  user.subscribe((value) => {
    if (value) name = value.name;
  });

  user.subscribe((value) => {
    if (value) surname = value.surname;
  });

  user.subscribe((value) => {
    if (value) patronymic = value.patronymic || "";
  });

  user.subscribe((value) => {
    if (value) email = value.email;
  });

  user.subscribe((value) => {
    if (value) classNumber = value.classNumber;
  });

  let nameError = false,
    surnameError = false,
    patronymicError = false,
    emailError = false,
    passwordError = false,
    currentPasswordError = false,
    repeatPasswordError = false;

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

  let showCurrentPassword = false,
    showPassword = false,
    showRepeatPassword = false;

  let disabled = false,
    passwordDisabled = false;

  let passwordEditActive = false;

  function checkRules(value, rules) {
    let errors = rules
      .map((r) => r(value))
      .filter((r) => typeof r === "string");

    if (errors.length) return true;
    return false;
  }

  function save_changes() {
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

    if (emailError || nameError || surnameError || patronymicError) {
      disabled = false;
      return;
    }

    axios({
      method: "put",
      url: "APP.API/user",
      headers: {
        "X-Requested-With": "XMLHttpRequest",
        "Content-Type": "application/json",
        Authorization: `Token ${$user.token}`,
      },
      data: {
        user: {
          email: email,
          name: name,
          surname: surname,
          patronymic: patronymic == "" ? null : patronymic,
          classNumber: classNumber,
        },
      },
      withCredentials: true,
    })
      .then((response) => {
        let token = $user.token;
        response.data.user.token = token;
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

  function change_password() {
    passwordDisabled = true;

    requestError = false;
    requestErrorMessage = "";

    password = password.trim();
    currentPassword = currentPassword.trim();
    repeatPassword = repeatPassword.trim();

    currentPasswordError = checkRules(currentPassword, passwordRules);
    passwordError = checkRules(password, passwordRules);
    repeatPasswordError = checkRules(repeatPassword, passwordRules);

    if (currentPasswordError || passwordError || repeatPasswordError) {
      disabled = false;
      return;
    }

    if (password != repeatPassword) {
      disabled = false;
      return;
    }

    axios({
      method: "put",
      url: "APP.API/user/password",
      headers: {
        "X-Requested-With": "XMLHttpRequest",
        "Content-Type": "application/json",
        Authorization: `Token ${$user.token}`,
      },
      data: {
        user: {
          email: $user.email,
          oldPassword: currentPassword,
          password: password,
        },
      },
      withCredentials: true,
    })
      .then((response) => {
        let token = $user.token;
        response.data.user.token = token;
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
        passwordDisabled = false;
      });
  }
</script>

<Container class="text-center" style="max-width:600px;">
  <Card class="pa-4" style="background: #fefefe">
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

    <p class="pb-4">Класс</p>
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

    <Button
      class="primary-color"
      on:click={save_changes}
      {disabled}
      rounded
      size="large">
      {#if disabled == true}
        <Icon spin path={mdiLoading} />
        Загрузка...
      {:else}
        <Icon path={mdiAccountEdit} />
        Сохранить изменения
      {/if}
    </Button>
  </Card>

  <div class="d-flex justify-center mt-4 mb-4">
    <Card style="max-width:600px; width: 100%">
      <CardTitle>
        <div class="d-flex justify-space-between" style="width: 100%">
          <p>
            <Icon path={mdiLock} class="default-icon" />
            Изменить пароль
          </p>
          <div class="slider-button">
            <Button
              text
              fab
              size="small"
              class="ml-auto"
              on:click={() => {
                passwordEditActive = !passwordEditActive;
              }}>
              <Icon
                path={mdiChevronDown}
                rotate={passwordEditActive ? 180 : 0} />
            </Button>
          </div>
        </div>
      </CardTitle>
      {#if passwordEditActive}
        <Divider />
        <div transition:slide class="pl-4 pr-4 pt-2 pb-2">
          <TextField
            bind:value={currentPassword}
            counter={255}
            rules={passwordRules}
            error={currentPasswordError}
            class="mb-2"
            outlined
            rounded
            type={showCurrentPassword ? 'text' : 'password'}>
            <div slot="prepend">
              <Icon path={mdiFormTextboxPassword} />
            </div>
            Текущий пароль
            <div slot="append">
              <Button
                icon
                class="primary-text"
                on:click={() => {
                  showCurrentPassword = !showCurrentPassword;
                }}>
                <Icon
                  class="default-icon"
                  path={showCurrentPassword ? mdiEyeOff : mdiEye} />
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
            Новый пароль
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
            Повторите новый пароль
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
        </div>

        <Button
          class="primary-color mb-4"
          on:click={change_password}
          {disabled}
          rounded
          size="large">
          {#if passwordDisabled == true}
            <Icon spin path={mdiLoading} />
            Загрузка...
          {:else}
            <Icon path={mdiKeyChainVariant} />
            Изменить пароль
          {/if}
        </Button>
      {/if}
    </Card>
  </div>
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
    class="primary-color ma-2 text-left"
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
