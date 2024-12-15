<template>
  <div>
    <h1>account</h1>

    <h3>Name: {{ user.firstName }} {{ user.lastName}}</h3>
  </div>
</template>

<script>
import {mapState} from "vuex";

export default {
  computed: {
    ...mapState({
      user: state => state.user,
      token: state => state.token
    })
  },
}
</script>