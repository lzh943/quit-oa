<template>
  <div>
    <div>账号切换</div>
    <button @click="zhangsan()" type="default" size="mini">张三</button>
    <button @click="lisi()" type="default" size="mini">李四</button>
    <button @click="zzjl()" type="default" size="mini">张总经理</button>
    <button @click="admin()" type="default" size="mini">admin</button>
    <div>当前token：{{ token }}</div>
  </div>
</template>

<script>
export default {
  name: "Test",

  data() {
    return {
      token: ''
    };
  },

  created(){
    this.token = window.localStorage.getItem('token')
  },

  methods: {
    zhangsan() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwMDCzNDY00VEqLU4t8kxRsjKHMPMSc1OBWqoyEvPSixPzlGoBW3mhH0UAAAA.qI0rZ1cr6LDjmogFjjjQj8m2XMahN_f5gWs7fFnLUpkguS1yJnN-shH1190FCF9jGZlfaJYcsIXxmC9VaD9Aqw'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    lisi() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDM3NjMwNDY10VEqLU4t8kxRsjKGMPMSc1OBWnKKirNylGoBjn551kIAAAA.HiQAK2uUQCFlQilO3mR3p5TURrlMrjN7zrR1IcFqhU8Q-VyxUiRJrGIuSwSKOC1uyQH_4JXL99yGDq74OnKvXQ'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    zzjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDM3NjMwNLYw11EqLU4t8kxRsjKFMPMSc1OBWqqqsnKUagGHebUNQQAAAA.FcCkD6QfaP8bd_3MYsRD6Tp9wr9JSRFWIUuIdG5O7XxhIeyWfzGLp40_Lesx_kWMMaaskh_ba3NrTlj8-nP6OA'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    admin() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwMDC0MLY001EqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQC8HXqFQgAAAA.gCsRVUJ7efefMINpTVzNKiko18X2wzxFe54cQlzx-TL2DUmbWIK-dx8Er0NsR4jN-IpJ9SXzosh3HIHUtkJCXw'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    }
  }
}
</script>
