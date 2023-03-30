<template>
  <div class="container">
    <van-nav-bar
      title="关于我们"
    />
    <div class="detail-wrap">
      <div class="item">
        此项目采用前后端分离的方式进行开发，并整合了微信公众号。
        主要技术:SpringBoot+Mybatis-Plus+Activity7+SpringSecurity+Redis,Vue
        通过此项目对工作流有了更深的了解,也进一步加深了对权限控制的理解

      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "process",

  data() {
    return {
    };
  },

  created(){
  },

  methods: {

  }
}
</script>

<style lang="scss" scoped>
.container {
  padding: 20px;
  .detail-wrap {
    .item {
      color: #838485;
    }
  }
}
</style>
