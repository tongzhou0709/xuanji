<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <router-view :key="key" />
    </transition>
  </section>
</template>

<script>
export default {
  name: 'AppMain',
  computed: {
    key() {
      return this.$route.path
    }
  }
}
</script>

<style scoped>
.app-main {
  flex: 1;
  width: 100%;
  position: relative;
  overflow-y: auto;
  background-color: #f0f2f5;
}

/* 移除旧的 padding-top 逻辑，因为现在使用 flex 布局 */
.fixed-header+.app-main {
  padding-top: 0;
}
</style>
