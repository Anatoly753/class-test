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
  mdiGithub,
} from "@mdi/js";
import Page from "../../components/Page.svelte";
import Row from "svelte-materialify/src/components/Grid/Row.svelte";
import Col from "svelte-materialify/src/components/Grid/Col.svelte";
import Tooltip from "svelte-materialify/src/components/Tooltip";
import Avatar from "svelte-materialify/src/components/Avatar";
import { goto } from "@sapper/app";
</script>

<svelte:head>
  <title>О проекте</title>
</svelte:head>

<Page>
  <Container class="text-center" style="max-width:1200px;">
    <Card class="pa-4" style="background: #fefefe">
      <h4>О проекте</h4>

      <hr
        class="mt-1 mb-8"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />

      <Row>
        <Col>
          <Tooltip bottom>
            <span slot="tip">Debian</span>
            <!-- <div
              class={answerType == "text"
                ? "answerType-active"
                : "answerType"}
              on:click={() => {
                answerType = "text";
                setSettingForCurrentQuestion("answerType", "text");
              }}> -->
            <Avatar size={70} tile>
              <img
                src="/about/debian-min.png"
                alt="Debian"
                style="width: 70px; height: 70px;" />
            </Avatar>
            <!-- </div> -->
          </Tooltip>
        </Col>
        <Col>
          <Tooltip bottom>
            <span slot="tip">Docker</span>
            <!-- <div
              class={answerType == "text"
                ? "answerType-active"
                : "answerType"}
              on:click={() => {
                answerType = "text";
                setSettingForCurrentQuestion("answerType", "text");
              }}> -->
            <Avatar size={70} tile>
              <img
                src="/about/docker-min.png"
                alt="Docker"
                style="width: 70px; height: 70px;" />
            </Avatar>
            <!-- </div> -->
          </Tooltip>
        </Col>
        <Col>
          <Tooltip bottom>
            <span slot="tip">Kotlin</span>
            <!-- <div
              class={answerType == "text"
                ? "answerType-active"
                : "answerType"}
              on:click={() => {
                answerType = "text";
                setSettingForCurrentQuestion("answerType", "text");
              }}> -->
            <Avatar size={70} tile>
              <img
                src="/about/kotlin.png"
                alt="Kotlin"
                style="width: 60px; height: 60px;" />
            </Avatar>
            <!-- </div> -->
          </Tooltip>
        </Col>
        <Col>
          <Tooltip bottom>
            <span slot="tip">Gradle</span>
            <!-- <div
              class={answerType == "text"
                ? "answerType-active"
                : "answerType"}
              on:click={() => {
                answerType = "text";
                setSettingForCurrentQuestion("answerType", "text");
              }}> -->
            <Avatar size={70} tile>
              <img
                src="/about/gradle-min.png"
                alt="Gradle"
                style="width: 70px; height: 70px;" />
            </Avatar>
            <!-- </div> -->
          </Tooltip>
        </Col>
        <Col>
          <Tooltip bottom>
            <span slot="tip">Sapper</span>
            <!-- <div
              class={answerType == "text"
                ? "answerType-active"
                : "answerType"}
              on:click={() => {
                answerType = "text";
                setSettingForCurrentQuestion("answerType", "text");
              }}> -->
            <Avatar size={70} tile>
              <img
                src="/about/sapper.png"
                alt="Sapper"
                style="width: 70px; height: 70px;" />
            </Avatar>
            <!-- </div> -->
          </Tooltip>
        </Col>
        <Col>
          <Tooltip bottom>
            <span slot="tip">Rollup</span>
            <!-- <div
              class={answerType == "text"
                ? "answerType-active"
                : "answerType"}
              on:click={() => {
                answerType = "text";
                setSettingForCurrentQuestion("answerType", "text");
              }}> -->
            <Avatar size={70} tile>
              <img
                src="/about/rollup-min.png"
                alt="Rollup"
                style="width: 60px; height: 60px;" />
            </Avatar>
            <!-- </div> -->
          </Tooltip>
        </Col>
        <Col>
          <Tooltip bottom>
            <span slot="tip">Caddy</span>
            <!-- <div
              class={answerType == "text"
                ? "answerType-active"
                : "answerType"}
              on:click={() => {
                answerType = "text";
                setSettingForCurrentQuestion("answerType", "text");
              }}> -->
            <Avatar size={70} tile>
              <img
                src="/about/caddy.png"
                alt="Caddy"
                style="width: 70px; height: 70px;" />
            </Avatar>
            <!-- </div> -->
          </Tooltip>
        </Col>
        <Col>
          <Tooltip bottom>
            <span slot="tip">MariaDB</span>
            <!-- <div
              class={answerType == "text"
                ? "answerType-active"
                : "answerType"}
              on:click={() => {
                answerType = "text";
                setSettingForCurrentQuestion("answerType", "text");
              }}> -->
            <Avatar size={70} tile>
              <img
                src="/about/mariadb.png"
                alt="MariaDB"
                style="width: 80px; height: 80px;" />
            </Avatar>
            <!-- </div> -->
          </Tooltip>
        </Col>
        <Col>
          <Tooltip bottom>
            <span slot="tip">MeiliSearch</span>
            <!-- <div
              class={answerType == "text"
                ? "answerType-active"
                : "answerType"}
              on:click={() => {
                answerType = "text";
                setSettingForCurrentQuestion("answerType", "text");
              }}> -->
            <Avatar size={70} tile>
              <img
                src="/about/meili.png"
                alt="MeiliSearch"
                style="width: 70px; height: 70px;" />
            </Avatar>
            <!-- </div> -->
          </Tooltip>
        </Col>
        <Col>
          <Tooltip bottom>
            <span slot="tip">HTML5</span>
            <!-- <div
              class={answerType == "text"
                ? "answerType-active"
                : "answerType"}
              on:click={() => {
                answerType = "text";
                setSettingForCurrentQuestion("answerType", "text");
              }}> -->
            <Avatar size={70} tile>
              <img
                src="/about/html-min.png"
                alt="HTML5"
                style="width: 70px; height: 70px;" />
            </Avatar>
            <!-- </div> -->
          </Tooltip>
        </Col>
        <Col>
          <Tooltip bottom>
            <span slot="tip">CSS3</span>
            <!-- <div
              class={answerType == "text"
                ? "answerType-active"
                : "answerType"}
              on:click={() => {
                answerType = "text";
                setSettingForCurrentQuestion("answerType", "text");
              }}> -->
            <Avatar size={70} tile>
              <img
                src="/about/css-min.png"
                alt="CSS3"
                style="width: 70px; height: 70px;" />
            </Avatar>
            <!-- </div> -->
          </Tooltip>
        </Col>
        <Col>
          <Tooltip bottom>
            <span slot="tip">JS</span>
            <!-- <div
              class={answerType == "text"
                ? "answerType-active"
                : "answerType"}
              on:click={() => {
                answerType = "text";
                setSettingForCurrentQuestion("answerType", "text");
              }}> -->
            <Avatar size={70} tile>
              <img
                src="/about/js-min.png"
                alt="JS"
                style="width: 70px; height: 70px;" />
            </Avatar>
            <!-- </div> -->
          </Tooltip>
        </Col>
      </Row>
      <p class="text-center">
        <Button
          class="grey darken-4 white-text"
          size="large"
          on:click={() => {
            goto("https://github.com/Anatoly753/class-test");
          }}>
          <Icon path={mdiGithub} />
          <b style="font-size: 1.1em;"> GitHub</b>
        </Button>
      </p>

      <a href="https://www.flaticon.com/">Flaticon</a>
      <a href="https://www.freepik.com/">Freepik</a>
      <a href="https://unsplash.com/">Unsplash</a>
      <a href="https://icon-icons.com/ru/">Icon-icons</a>
      <a href="https://www.pexels.com/ru-ru/">Pexels</a>
    </Card>
  </Container>
</Page>

<style>
</style>
