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
  mdiAccountHardHat,
  mdiCoffee,
  mdiPlaylistPlus,
  mdiFormatListCheckbox,
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
  "/sign_up": { id: 3, name: "Регистрация", icon: mdiAccountPlus },
  "/sign_in": { id: 4, name: "Вход", icon: mdiLogin },
  "/profile": { id: 3, name: "Профиль", icon: mdiAccountCircle },
  "/constructor": {
    id: 4,
    name: "Конструктор тестов",
    icon: mdiCoffee,
    "/create": { id: 4, name: "Создать тест", icon: mdiPlaylistPlus },
    "/tests": { id: 4, name: "Мои тесты", icon: mdiFormatListCheckbox },
  },
  "/about": {
    id: 2,
    name: "Информация",
    icon: mdiInformation,
    "/help": { id: 2, name: "Помощь", icon: mdiInformation },
    "/feedback": { id: 2, name: "Написать", icon: mdiEmailEdit },
    "/project": { id: 2, name: "О проекте", icon: mdiBriefcase },
  },
};

let drawerItems = [];

const links = ["/", "/tests" /*"/about"*/];

export let segment;
segment = segment;
</script>

<MaterialApp theme={theme}>
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
      <div class="logo" on:click={() => goto("/")}>
        <img src="logo.svg" alt="ClassTest logo" />
        <!-- <img src="logo-512-min.png" alt="ClassTest logo" /> -->
        <b class="white-text">ClassTest</b>
      </div>

      <!-- <div class="logo" on:click={() => goto("/")}>
        <object
          type="image/svg+xml"
          data="logo.svg"
          id="object"
          title="ClassTest logo"
        />
        <a href="." class="white-text">ClassTest</a>
      </div> -->

      <div style="flex-grow:1" />
      <div class="desktop-menu d-none d-md-inline">
        <Button
          text
          rounded
          active={$page.path == "/"}
          class="white-text"
          on:click={() => {
            goto("/");
          }}>
          <Icon path={routes["/"].icon} />
          {routes["/"].name}
        </Button>

        <Button
          text
          rounded
          active={$page.path == "/tests"}
          class="white-text"
          on:click={() => {
            goto("/tests");
          }}>
          <Icon path={routes["/tests"].icon} />
          {routes["/tests"].name}
        </Button>

        {#if $user == undefined}
          <Menu right hover class="primary-color elevation-8">
            <div slot="activator">
              <Button
                text
                rounded
                active={$page.path == "/profile"}
                class="white-text">
                <Icon path={routes["/profile"].icon} />
                {routes["/profile"].name}
              </Button>
            </div>
            <List>
              <ListItem
                class="white-text"
                active={$page.path == "/sign_up"}
                on:click={() => {
                  goto("/sign_up");
                }}>
                <span slot="prepend">
                  <Icon class="white-text" path={routes["/sign_up"].icon} />
                </span>
                <p
                  style="font-size: 0.9em; font-weight: 600; line-height: 24px; display: table-cell; vertical-align: middle;">
                  {routes["/sign_up"].name}
                </p>
              </ListItem>

              <ListItem
                class="white-text"
                active={$page.path == "/sign_in"}
                on:click={() => {
                  goto("/sign_in");
                }}>
                <span slot="prepend">
                  <Icon class="white-text" path={routes["/sign_in"].icon} />
                </span>
                <p
                  style="font-size: 0.9em; font-weight: 600; line-height: 24px; display: table-cell; vertical-align: middle;">
                  {routes["/sign_in"].name}
                </p>
              </ListItem>
            </List>
          </Menu>
        {:else}
          <Button
            text
            rounded
            active={$page.path == "/profile"}
            class="white-text"
            on:click={() => {
              goto("/profile");
            }}>
            <Icon path={routes["/profile"].icon} />
            {routes["/profile"].name}
          </Button>

          {#if $user.userRole == "editor"}
            <Menu right hover class="primary-color elevation-8">
              <div slot="activator">
                <Button text rounded class="white-text">
                  <Icon path={routes["/constructor"].icon} />
                  {routes["/constructor"].name}
                </Button>
              </div>

              <List>
                <ListItem
                  class="white-text"
                  active={$page.path == "/constructor/create"}
                  on:click={() => {
                    goto("/constructor/create");
                  }}>
                  <span slot="prepend">
                    <Icon
                      class="white-text"
                      path={routes["/constructor"]["/create"].icon} />
                  </span>
                  <p
                    style="font-size: 0.9em; font-weight: 600; line-height: 24px; display: table-cell; vertical-align: middle;">
                    {routes["/constructor"]["/create"].name}
                  </p>
                </ListItem>

                <ListItem
                  class="white-text"
                  active={$page.path == "/constructor/tests"}
                  on:click={() => {
                    goto("/constructor/tests");
                  }}>
                  <span slot="prepend">
                    <Icon
                      class="white-text"
                      path={routes["/constructor"]["/tests"].icon} />
                  </span>
                  <p
                    style="font-size: 0.9em; font-weight: 600; line-height: 24px; display: table-cell; vertical-align: middle;">
                    {routes["/constructor"]["/tests"].name}
                  </p>
                </ListItem>
              </List>
            </Menu>
          {/if}
        {/if}

        <Menu right hover class="primary-color elevation-8">
          <div slot="activator">
            <Button text rounded class="white-text">
              <Icon path={routes["/about"].icon} />
              {routes["/about"].name}
            </Button>
          </div>
          <List>
            <!-- <ListItem
              class="white-text"
              active={$page.path == "/about/help"}
              on:click={() => {
                goto("/about/help");
              }}
            >
              <span slot="prepend">
                <Icon
                  class="white-text"
                  path={routes["/about"]["/help"].icon}
                />
              </span>
              <p
                style="font-size: 0.9em; font-weight: 600; line-height: 24px; display: table-cell; vertical-align: middle;"
              >
                {routes["/about"]["/help"].name}
              </p>
            </ListItem> -->

            <!-- <ListItem
              class="white-text"
              active={$page.path == "/about/feedback"}
              on:click={() => {
                goto("/about/feedback");
              }}>
              <span slot="prepend">
                <Icon
                  class="white-text"
                  path={routes["/about"]["/feedback"].icon} />
              </span>
              <p
                style="font-size: 0.9em; font-weight: 600; line-height: 24px; display: table-cell; vertical-align: middle;">
                {routes["/about"]["/feedback"].name}
              </p>
            </ListItem> -->

            <ListItem
              class="white-text"
              active={$page.path == "/about/project"}
              on:click={() => {
                goto("/about/project");
              }}>
              <span slot="prepend">
                <Icon
                  class="white-text"
                  path={routes["/about"]["/project"].icon} />
              </span>
              <p
                style="font-size: 0.9em; font-weight: 600; line-height: 24px; display: table-cell; vertical-align: middle;">
                {routes["/about"]["/project"].name}
              </p>
            </ListItem>
          </List>
        </Menu>
      </div>
    </AppBar>
  </div>

  <NavigationDrawer absolute active={active} style="z-index: 10;">
    <List nav dense>
      <ListItemGroup
        mandatory
        value={routes[$page.path] ? routes[$page.path].id : undefined}>
        <ListItem
          on:click={() => {
            goto("/");
            toggleNavigation();
          }}>
          <span slot="prepend">
            <Icon path={routes["/"].icon} />
          </span>
          {routes["/"].name}
        </ListItem>

        <ListItem
          on:click={() => {
            goto("/tests");
            toggleNavigation();
          }}>
          <span slot="prepend">
            <Icon path={routes["/tests"].icon} />
          </span>
          {routes["/tests"].name}
        </ListItem>

        <ListItem
          on:click={() => {
            goto("/about/project");
            toggleNavigation();
          }}>
          <span slot="prepend">
            <Icon path={routes["/about"]["/project"].icon} />
          </span>
          {routes["/about"]["/project"].name}
        </ListItem>

        <Divider />
        <Subheader>Аккаунт</Subheader>

        {#if $user == undefined}
          <ListItem
            on:click={() => {
              goto("/sign_up");
              toggleNavigation();
            }}>
            <span slot="prepend">
              <Icon path={routes["/sign_up"].icon} />
            </span>
            {routes["/sign_up"].name}
          </ListItem>

          <ListItem
            on:click={() => {
              goto("/sign_in");
              toggleNavigation();
            }}>
            <span slot="prepend">
              <Icon path={routes["/sign_in"].icon} />
            </span>
            {routes["/sign_in"].name}
          </ListItem>
        {:else}
          <ListItem
            on:click={() => {
              goto("/profile");
              toggleNavigation();
            }}>
            <span slot="prepend">
              <Icon path={routes["/profile"].icon} />
            </span>
            {routes["/profile"].name}
          </ListItem>

          {#if $user.userRole == "editor"}
            <Divider />
            <Subheader>Редактор тестов</Subheader>

            <ListItem
              active={$page.path == "/constructor/create"}
              on:click={() => {
                goto("/constructor/create");
                toggleNavigation();
              }}>
              <span slot="prepend">
                <Icon path={routes["/constructor"]["/create"].icon} />
              </span>
              {routes["/constructor"]["/create"].name}
            </ListItem>

            <ListItem
              active={$page.path == "/constructor/tests"}
              on:click={() => {
                goto("/constructor/tests");
                toggleNavigation();
              }}>
              <span slot="prepend">
                <Icon path={routes["/constructor"]["/tests"].icon} />
              </span>
              {routes["/constructor"]["/tests"].name}
            </ListItem>
          {/if}
        {/if}
      </ListItemGroup>
    </List>
  </NavigationDrawer>
  <Overlay active={active} absolute on:click={toggleNavigation} index={6} />

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
        <b>Copyright &copy 2020-2021 - ClassTest</b>
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
