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
  } from "@mdi/js";
  import Page from "../../components/Page.svelte";
</script>

<svelte:head>
  <title>Помощь</title>
</svelte:head>

<Page>
  <Container class="text-center" style="max-width:1200px;">
    <Card class="pa-4" style="background: #fefefe">
      <h4>Помощь</h4>

      <hr
        class="mt-1 mb-8"
        style="border: 0; height: 1px; background-image: linear-gradient(to
        right, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0));" />
    </Card>
  </Container>
</Page>

<style>
</style>
