<template>
  <div class="min-h-screen">
    <div class="flex flex-col justify-center items-center h-5/6">
      <div class="avatar m-10 mt-20">
        <div class="w-24">
          <img :src="lock" alt="Icon"/>
        </div>
      </div>
      <div>
        <a class="btn btn-ghost normal-case text-2xl">Вход в Teamsync</a>
      </div>
      <p class="text-base-content mb-5">
        <strong>Сервис для комфортной работы</strong>
      </p>

      <text-input
          label="Вы уже ввели свою почту"
          placeholder=""
          :model-value="email"
          error-message=""
          :is-error="false"
          :is-input-disabled="true"
          :is-input-readonly="true"
      />

      <text-input
          label="Введите пароль"
          placeholder="Придумайте пароль"
          :model-value="this.password"
          :is-error="this.isPasswordMismatch"
          input-type="password"
          :is-input-required="true"
          @update="(value) => this.password = value"
      />

      <div>
        <button
            class="btn btn-warning btn-outline m-5"
            @click="this.$router.push({name: 'start_login_page'})"
        >
          Назад
          <ArrowUturnLeftIcon class="h-6 w-6"/>
        </button>
        <button
            class="btn btn-success m-5"
            :class="isPasswordInputEmpty || isRepeatPasswordInputEmpty ? 'btn-outline' : ''"
            @click="checkPassword"
        >
          Войти
          <LockOpenIcon class="h-6 w-6"/>
        </button>
      </div>
    </div>

    <sticky-bottom-element>
      <teamsync-button
          class="btn-primary"
          @click="this.$router.push({name: 'start_signup_page'})"
      >
        Зарегистрироваться
        <PlusCircleIcon class="h-6 w-6"/>
      </teamsync-button>
    </sticky-bottom-element>
  </div>
</template>

<script>
import TextInput from "@/components/ui/TextInput.vue";
import {ArrowRightOnRectangleIcon, ArrowUturnLeftIcon, LockOpenIcon, PlusCircleIcon} from "@heroicons/vue/24/outline";
import lockImage from "/src/static/lock-dynamic-color-png.png"
import TeamsyncButton from "@/components/ui/TeamsyncButton.vue";
import StickyBottomElement from "@/components/ui/StickyBottomElement.vue";
import axios from "axios";
import Constants from "@/components/constants";

export default {
  components: {
    StickyBottomElement,
    ArrowRightOnRectangleIcon,
    TeamsyncButton,
    TextInput, ArrowUturnLeftIcon, LockOpenIcon, PlusCircleIcon
  },
  data() {
    return {
      email: this.$route.query.email,
      password: '',
      isPasswordInputEmpty: true,
      isRepeatPasswordInputEmpty: true,
      isPasswordMismatch: false,
      errorMessage: '',
      isSuccessLogin: false,

      lock: lockImage
    }
  },
  watch: {
    password(value) {
      this.password = value;
      if (this.password !== "") {
        this.isPasswordInputEmpty = false
      }
    }
  },
  methods: {
    checkPassword() {
      if (this.password === "") {
        this.isPasswordMismatch = true
        this.errorMessage = 'Заполните все поля для продолжения'
      } else {
        this.login(this.email, this.password)
      }
    },
    async login(email, password) {
      console.log(email, password)
      await axios.post(Constants.HOST + "/auth/v1/login/email", {
        email: email,
        password: password
      }).then((response) => {
        this.$store.commit('setAuth', 'true');
        const user = {
          id: response.data.payload.id,
          firstName: response.data.payload.firstName,
          lastName: response.data.payload.lastName,
          isUserAdmin: response.data.payload.isUserAdmin,
          gitHubToken: response.data.payload.gitHubToken
        }
        this.$store.commit('setUser', user);
        this.$store.commit('setToken', response.data.payload.token)

        if (user.gitHubToken !== null) {
          this.$store.commit('setGitHubToken', user.gitHubToken);
        }

        this.isAuth = true;

        this.isSuccessLogin = true;
        console.log(response.data.payload)
        this.$router.push({name: 'account_page'})
      }).catch((error) => {
        console.log(error)
        if (error.response.data.status === 400) {
          this.isPasswordMismatch = true;
          this.errorMessage = error.response.data.description;
        } else {
          this.isPasswordMismatch = true;
          this.errorMessage = "Что-то неприятное происходит с нашим сервером"
        }
      });
    }
  }
}
</script>