<script>
  import PageTransition from "./../components/PageTransition.svelte";
  import {
    Container,
    Button,
    Card,
    CardTitle,
    CardSubtitle,
    CardActions,
    Row,
    Col,
  } from "svelte-materialify/src";

  const songs = [
    { name: "Tuba Angel", author: "Иοκτцяηаι", color: "blue" },
    { name: "Tuba Mech", author: "Noah Giesler", color: "red" },
    { name: "Tuba Mech 2", author: "Noah Giesler", color: "purple" },
    { name: "Tuba Archmage", author: "Noah Giesler", color: "green" },
    { name: "Tuba Archmage 2", author: "Noah Giesler", color: "orange" },
    { name: "Tuba Archmage 2", author: "Noah Giesler", color: "teal" },
    { name: "Tuba Archmage 2", author: "Noah Giesler", color: "cyan" },
  ];
</script>

<svelte:head>
  <title>About</title>
</svelte:head>

<PageTransition>
  <Container class="text-center">
    <h2 class="mb-2">About</h2>

    <Button class="primary-color">Primary</Button>
    <Button class="secondary-color">Secondry</Button>
    <Button class="button-color">Button</Button>
    <Button>Default</Button>
  </Container>

  <div class="d-flex justify-center mt-4 mb-4">
    <div
      class="rounded elevation-2 theme--light pa-4 d-flex d-flex justify-center flex-column"
      style="flex-grow:1;max-width:600px;">
      {#each songs as song, i}
        <Card class={song.color} style="max-width:400px;">
          <Row>
            <Col cols={8}>
              <CardTitle class="white-text">{song.name}</CardTitle>
              <CardSubtitle class="white-text">{song.author}</CardSubtitle>
              <CardActions>
                <Button text class="white-text">Play</Button>
              </CardActions>
            </Col>
            <Col cols={4}>
              <img
                class="rounded"
                src="//picsum.photos/100?random={i}"
                alt="cover" />
            </Col>
          </Row>
        </Card>
        <br />
      {/each}
    </div>
  </div>
</PageTransition>