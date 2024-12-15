<template>
  <div class="min-h-screen">
    <div class="flex flex-col justify-center items-center h-5/6">
      <div class="avatar m-10 mt-20">
        <div class="w-24">
          <img :src="fire" alt="Icon"/>
        </div>
      </div>
      <div>
        <a class="btn btn-ghost normal-case text-2xl">Регистрация в Teamsync</a>
      </div>
      <p class="text-base-content mb-5">
        <strong>{{ this.firstName }}, Вы почти у цели!</strong>
      </p>

      <text-input
          label="Вы уже ввели свою почту"
          placeholder=""
          :model-value="this.email"
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

      <text-input
          label="Повторите свой пароль"
          placeholder="Просто для проверки"
          :model-value="this.repeatPassword"
          :error-message="this.errorMessage"
          :is-error="this.isPasswordMismatch"
          input-type="password"
          :is-input-required="true"
          @update="(value) => this.repeatPassword = value"
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
          Поехали
          <CheckIcon class="h-6 w-6"/>
        </button>
      </div>
    </div>

    <sticky-bottom-element>
      <teamsync-button
          class="btn-info"
          @click="this.$router.push({name: 'start_login_page'})"
      >
        Войти
        <ArrowRightOnRectangleIcon class="h-6 w-6"/>
      </teamsync-button>
    </sticky-bottom-element>
  </div>
</template>

<script>
import TeamsyncButton from "@/components/ui/TeamsyncButton.vue";
import TextInput from "@/components/ui/TextInput.vue";
import {
  ArrowRightCircleIcon,
  ArrowRightOnRectangleIcon,
  ArrowUturnLeftIcon,
  CheckIcon
} from "@heroicons/vue/24/outline";
import StickyBottomElement from "@/components/ui/StickyBottomElement.vue";
import fireImage from "/src/static/fire-dynamic-color-png.png"

export default {
  components: {
    ArrowUturnLeftIcon, StickyBottomElement, ArrowRightCircleIcon, ArrowRightOnRectangleIcon,
    TextInput, TeamsyncButton, CheckIcon
  },
  data() {
    return {
      email: this.$route.query.email,
      firstName: this.$route.query.firstName,
      lastName: this.$route.query.lastName,

      password: '',
      repeatPassword: '',
      isPasswordInputEmpty: true,
      isRepeatPasswordInputEmpty: true,
      isPasswordMismatch: false,
      errorMessage: '',

      fire: fireImage
    }
  },
  watch: {
    password(value) {
      this.password = value;
      if (this.password !== "") {
        this.isPasswordInputEmpty = false
      }
    },
    repeatPassword(value) {
      this.repeatPassword = value;
      if (this.repeatPassword !== "") {
        this.isRepeatPasswordInputEmpty = false
      }
    }
  },
  methods: {
    checkPassword() {
      if (this.password !== this.repeatPassword) {
        this.isPasswordMismatch = true
        this.errorMessage = 'Пароли не совпадают, попробуйте еще раз'
        this.password = ''
        this.repeatPassword = ''
      } else if (this.password === "" || this.repeatPassword === "") {
        this.isPasswordMismatch = true
        this.errorMessage = 'Заполните все поля для продолжения'
      } else {
        this.signUp(this.email, this.firstName, this.lastName, this.password)
      }
    },
    signUp(email, firstName, lastName, password) {
      console.log(email, firstName, lastName, password)
    }
  }
}
</script>