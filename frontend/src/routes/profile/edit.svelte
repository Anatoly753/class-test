<script>
  import Page from "../../components/Page.svelte";
  import {
    Container,
    Button,
    TextField,
    Checkbox,
    Icon,
    Card,
    Overlay,
    Alert,
    Tabs,
    Tab,
    TabContent,
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
    mdiAccountEdit,
    mdiCardAccountDetailsOutline,
    mdiCog,
  } from "@mdi/js";
  import { user } from "../_store.js";
  import { goto } from "@sapper/app";
  import { onMount } from "svelte";

  import axios from "axios";
  import EditProfileData from "../../components/profile/editProfileData.svelte";

  // onMount(async () => {
  //   console.log($user.email + '!')
  // })
</script>

<svelte:head>
  <title>Профиль</title>
</svelte:head>

<Page authenticationRequired>
  <Container class="text-center" style="max-width:1200px;">
    <Card class="pa-4" style="background: #fefefe">
      <Tabs icons class="primary-color secondary-text theme--dark">
        <div slot="tabs">
          <Tab class="white-text">
            <Icon path={mdiCardAccountDetailsOutline} />
            Профиль
          </Tab>
          <Tab class="white-text">
            <Icon path={mdiCog} />
            Настройки
          </Tab>
        </div>
        <TabContent>
          <div class="mt-2">
            <EditProfileData />
          </div>
        </TabContent>
        <TabContent>
          <br />
          Item Content
          <p>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec
            accumsan, diam et elementum gravida, arcu mi fermentum nibh, vel
            dapibus ligula orci non est. Morbi commodo sagittis finibus.
            Maecenas in volutpat massa. Nullam vulputate metus velit, quis
            interdum elit imperdiet ut. Suspendisse et sagittis erat, euismod
            vulputate enim. Etiam feugiat sit amet justo vitae commodo.
          </p>
        </TabContent>
      </Tabs>
    </Card>
  </Container>
</Page>
