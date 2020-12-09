<script>
  import {
    mdiMenu,
    mdiHome,
    mdiLogin,
    mdiAccountPlus,
    mdiOrderBoolAscendingVariant,
    mdiInformation,
    mdiFileSearchOutline,
    mdiAccountCircle,
    mdiEmailEdit,
    mdiBriefcase,
  } from "@mdi/js";
  import {
    MaterialApp,
    AppBar,
    Button,
    Icon,
    NavigationDrawer,
    Overlay,
    List,
    ListItemGroup,
    ListItem,
    ListGroup,
    Subheader,
    Divider,
    Footer,
    Menu,
  } from "svelte-materialify/src";

  import { user } from "./_store.js";
  import { goto } from "@sapper/app";
  import { stores } from "@sapper/app";
  const { preloading, page, session } = stores();

  let theme = "light";

  let active = false;

  function toggleNavigation() {
    active = !active;
  }

  let routes = {
    "/": { id: 0, name: "Главная", icon: mdiHome },
    "/tests": { id: 1, name: "Тесты", icon: mdiOrderBoolAscendingVariant },
    "/sign_up": { id: 2, name: "Регистрация", icon: mdiAccountPlus },
    "/sign_in": { id: 3, name: "Вход", icon: mdiLogin },
    "/profile": { id: 2, name: "Профиль", icon: mdiAccountCircle },
    "/about": {
      id: 4,
      name: "Информация",
      icon: mdiInformation,
      "/help": { id: 4, name: "Помощь", icon: mdiInformation },
      "/feedback": { id: 4, name: "Написать", icon: mdiEmailEdit },
      "/project": { id: 4, name: "О проекте", icon: mdiBriefcase },
    },
  };

  const links = ["/", "/tests", "/about"];

  export let segment;
  segment = segment;
</script>

<MaterialApp {theme}>
  <div style="position:relative; z-index: 5;">
    <AppBar class="primary-color elevation-8">
      <div slot="icon" class="d-md-none">
        <Button
          fab
          text
          depressed
          class="white-text"
          on:click={toggleNavigation}>
          <Icon path={mdiMenu} />
        </Button>
      </div>
      <div class="logo" on:click={() => goto('/')}>
        <img src="logo.svg" alt="ClassTest logo" />
        <b class="white-text">ClassTest</b>
      </div>
      <!--
    <div class="logo" on:click={() => goto("/")} >
      <object
        type="image/svg+xml"
        data="logo.svg"
        id="object"
        title="ClassTest logo" />
      <a href="." class="white-text">ClassTest</a>
    </div>
    -->

      <div style="flex-grow:1" />
      <div class="desktop-menu d-none d-md-inline">
        <Button
          text
          rounded
          active={routes[$page.path] == routes['/']}
          class="white-text"
          on:click={() => {
            goto('/');
          }}>
          <Icon path={routes['/'].icon} />
          {routes['/'].name}
        </Button>

        <Button
          text
          rounded
          active={routes[$page.path] == routes['/tests']}
          class="white-text"
          on:click={() => {
            goto('/tests');
          }}>
          <Icon path={routes['/tests'].icon} />
          {routes['/tests'].name}
        </Button>

        {#if $user == undefined}
          <Menu right hover class="primary-color elevation-8">
            <div slot="activator">
              <Button
                text
                rounded
                active={routes[$page.path] == routes['/profile']}
                class="white-text">
                <Icon path={routes['/profile'].icon} />
                {routes['/profile'].name}
              </Button>
            </div>
            <List>
              <ListItem>
                <Button
                  text
                  rounded
                  active={routes[$page.path] == routes['/sign_up']}
                  on:click={() => {
                    goto('/sign_up');
                  }}
                  class="white-text">
                  <Icon path={routes['/sign_up'].icon} />
                  {routes['/sign_up'].name}
                </Button>
              </ListItem>
              <ListItem>
                <Button
                  text
                  rounded
                  active={routes[$page.path] == routes['/sign_in']}
                  on:click={() => {
                    goto('/sign_in');
                  }}
                  class="white-text">
                  <Icon path={routes['/sign_in'].icon} />
                  {routes['/sign_in'].name}
                </Button>
              </ListItem>
            </List>
          </Menu>
        {:else}
          <Button
            text
            rounded
            active={routes[$page.path] == routes['/profile']}
            class="white-text"
            on:click={() => {
              goto('/profile');
            }}>
            <Icon path={routes['/profile'].icon} />
            {routes['/profile'].name}
          </Button>
        {/if}

        <Menu right hover class="primary-color elevation-8">
          <div slot="activator">
            <Button
              text
              rounded
              active={routes[$page.path] == routes['/about']}
              class="white-text">
              <Icon path={routes['/about'].icon} />
              {routes['/about'].name}
            </Button>
          </div>
          <List>
            <ListItem
              on:click={() => {
                goto('/about/help');
              }}>
              <Button
                text
                rounded
                active={routes[$page.path] == routes['/about']['/help']}
                class="white-text">
                <Icon path={routes['/about']['/help'].icon} />
                {routes['/about']['/help'].name}
              </Button>
            </ListItem>
            <ListItem
              on:click={() => {
                goto('/about/feedback');
              }}>
              <Button
                text
                rounded
                active={routes[$page.path] == routes['/about']['/feedback']}
                class="white-text">
                <Icon path={routes['/about']['/feedback'].icon} />
                {routes['/about']['/feedback'].name}
              </Button>
            </ListItem>
            <ListItem
              on:click={() => {
                goto('/about/project');
              }}>
              <Button
                text
                rounded
                active={routes[$page.path] == routes['/about']['/project']}
                class="white-text">
                <Icon path={routes['/about']['/project'].icon} />
                {routes['/about']['/project'].name}
              </Button>
            </ListItem>
          </List>
        </Menu>
      </div>

      {#if $page.path == '/tests'}
        <Button fab text depressed class="white-text">
          <Icon path={mdiFileSearchOutline} />
        </Button>
      {/if}
    </AppBar>
  </div>

  <NavigationDrawer absolute {active} style="z-index: 10;">
    <List nav dense>
      <ListItemGroup
        mandatory
        value={routes[$page.path] ? routes[$page.path].id : undefined}>
        <ListItem
          on:click={() => {
            goto('/');
            toggleNavigation();
          }}>
          <span slot="prepend">
            <Icon path={routes['/'].icon} />
          </span>
          {routes['/'].name}
        </ListItem>

        <ListItem
          on:click={() => {
            goto('/tests');
            toggleNavigation();
          }}>
          <span slot="prepend">
            <Icon path={routes['/tests'].icon} />
          </span>
          {routes['/tests'].name}
        </ListItem>

        <ListItem
          on:click={() => {
            goto('/about');
            toggleNavigation();
          }}>
          <span slot="prepend">
            <Icon path={routes['/about'].icon} />
          </span>
          {routes['/about'].name}
        </ListItem>

        <Divider />
        <Subheader>Аккаунт</Subheader>

        {#if $user == undefined}
          <ListItem
            on:click={() => {
              goto('/sign_up');
              toggleNavigation();
            }}>
            <span slot="prepend">
              <Icon path={routes['/sign_up'].icon} />
            </span>
            {routes['/sign_up'].name}
          </ListItem>

          <ListItem
            on:click={() => {
              goto('/sign_in');
              toggleNavigation();
            }}>
            <span slot="prepend">
              <Icon path={routes['/sign_in'].icon} />
            </span>
            {routes['/sign_in'].name}
          </ListItem>
        {:else}
          <ListItem
            on:click={() => {
              goto('/profile');
              toggleNavigation();
            }}>
            <span slot="prepend">
              <Icon path={routes['/profile'].icon} />
            </span>
            {routes['/profile'].name}
          </ListItem>
        {/if}
      </ListItemGroup>
    </List>
  </NavigationDrawer>
  <Overlay {active} absolute on:click={toggleNavigation} index={6} />

  <div class="background" index={1}>
    <main>
      <slot />
    </main>
  </div>

  <footer>
    <Footer
      padless
      class="theme--dark justify-center flex-column"
      style="background-color: #1c212e;">
      <div class="mt-2 mb-2">
        {#each links as link}
          <Button text rounded on:click={() => goto(link)}>
            {routes[link].name}
          </Button>
        {/each}
      </div>
      <div class="pa-2 text-center" style="width:100%">
        <b>Copyright &copy 2020 - ClassTest</b>
      </div>
    </Footer>
  </footer>
</MaterialApp>

<style>
  .logo {
    display: inline;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.2s ease-in-out;
  }

  .logo:hover {
    transform: scale(0.9);
  }

  .logo img {
    width: 44px;
    height: 44px;
    margin-right: 8px;
  }

  .logo b {
    font-size: 1.3rem;
  }

  main {
    min-height: calc(100vh - 56px - 92px);
    padding-top: 20px;
    padding-bottom: 20px;
    /* background: #f8f8ff; */
    background-color: rgba(0, 0, 0, 0.03);
  }

  .background {
    background: url("/background/1.jpg");
    background-repeat: repeat;
  }

  @media (max-width: 600px) {
    .background {
      background-size: 450px;
    }
  }
</style>
