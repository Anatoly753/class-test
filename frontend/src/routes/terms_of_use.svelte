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
  } from "svelte-materialify/src";
  import {
    mdiAt,
    mdiEye,
    mdiEyeOff,
    mdiLoading,
    mdiLogin,
    mdiFormTextboxPassword,
    mdiInformationVariant,
  } from "@mdi/js";
  import { user } from "./_store.js";
  import { goto } from "@sapper/app";

  import axios from "axios";
</script>

<svelte:head>
  <title>Условия импользования</title>
</svelte:head>

<Page>
  <Container class="text-center" style="max-width:1400px;">
    <Card class="pa-4" style="background: #fefefe">
      <h4>Условия использования</h4>
      <hr
        class="mt-1 mb-8"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />
    </Card>
  </Container>
</Page>
