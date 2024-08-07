<template>
  <div class="container">
    <h1>Welcome</h1>
  </div>     
</template>

<script>
export default {
  name: "Home"
}
</script>

<style scoped>
  html, body {
    margin: 0;
    padding: 0;
    height: 100%;
  }

  .container {
    width: 100%;
    height: 100vh; 
    display: flex; 
    justify-content: center; 
    align-items: center; 
    --color: rgba(114, 114, 114, 0.3);
    background-color: #191a1a;
    background-image: linear-gradient(0deg, transparent 24%, var(--color) 25%, var(--color) 26%, transparent 27%, transparent 74%, var(--color) 75%, var(--color) 76%, transparent 77%, transparent),
        linear-gradient(90deg, transparent 24%, var(--color) 25%, var(--color) 26%, transparent 27%, transparent 74%, var(--color) 75%, var(--color) 76%, transparent 77%, transparent);
    background-size: 55px 55px;
    color: white; 
    text-align: center; 
  }
</style>

