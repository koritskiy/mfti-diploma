<template>
  <div class="min-h-screen">
    <div class="flex flex-col justify-center items-center h-4/6">
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
          label="Введите свою почту"
          placeholder="Например, alex@teamsync.ru"
          :model-value="this.email"
          :error-message="this.errorMessage"
          :is-error="this.isEmailError"
          input-type="email"
          :is-input-required="true"
          @update="(value) => this.email = value"
      />

      <div class="form-control w-full max-w-xs mt-5">
        <button
            class="btn btn-success"
            :class="isEmailInputEmpty ? 'btn-outline' : ''"
            @click="validateEmail(this.email)"
        >
          Продолжить
          <ArrowRightCircleIcon class="h-6 w-6"/>
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
import * as EmailValidator from 'email-validator';
import TextInput from "@/components/ui/TextInput.vue";
import {ArrowRightCircleIcon, PlusCircleIcon} from "@heroicons/vue/24/outline";
import lockImage from "/src/static/lock-dynamic-color-png.png"
import StickyBottomButton from "@/components/ui/TeamsyncButton.vue";
import StickyBottomElement from "@/components/ui/StickyBottomElement.vue";
import TeamsyncButton from "@/components/ui/TeamsyncButton.vue";

export default {
  components: {
    TeamsyncButton,
    StickyBottomElement,
    StickyBottomButton,
    TextInput,
    ArrowRightCircleIcon,
    PlusCircleIcon
  },
  data() {
    return {
      email: "",
      isEmailInputEmpty: true,
      errorMessage: "",
      isEmailError: false,
      lock: lockImage
    }
  },
  watch: {
    email(value) {
      this.email = value;
      if (this.email !== "") {
        this.isEmailInputEmpty = false
      }
    }
  },
  methods: {
    validateEmail(value) {
      if (EmailValidator.validate(value)) {
        this.$router.push({path: '/login/finish', query: {email: this.email}})
      } else {
        this.errorMessage = 'Некорректный адрес электронной почты';
        this.isEmailError = true;
      }
    }
  }
}
</script>