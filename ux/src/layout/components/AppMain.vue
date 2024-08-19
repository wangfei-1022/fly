<template>
  <section class="app-main" id="app-main">
    <transition name="fade-transform" mode="out-in">
      <keep-alive :include="cachedViews">
        <router-view v-if="!$route.meta.link" :key="key" />
      </keep-alive>
    </transition>
<!--    <div>{{ ttt }}</div>-->
  </section>
</template>

<script>
import watermark from "watermark-dom";

export default {
  name: "AppMain",
  data() {
    return {
      ttt: "",
    };
  },
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews;
    },
    key() {
      return this.$route.path;
    },
  },
  mounted() {
    // const FEI = this.Crypto.get("dg9i5Vj8iVKp/yNEySFZRQ==");
    // watermark.load({ watermark_txt: FEI });
  },
  watch: {
    $route(route) {
        this.ttt = this.Crypto.get(
          "JcPYFM5UAxqqLazbPKP8clN2geEWtOa/7P4nS4iJFvMeTCDKC0KgsFRoyO/kvjgmgzNB+L4ASDp26yz7hbhDxUpvtnLYSUgDUtII9L1Rxtk="
        );
    },
  },
};
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

.fixed-header + .app-main {
  padding-top: 50px;
}

.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    min-height: calc(100vh - 84px);
  }

  .fixed-header + .app-main {
    padding-top: 84px;
  }
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 17px;
  }
}
</style>
