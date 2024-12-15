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
        <strong>Сервис для комфортной работы</strong>
      </p>

      <text-input
          label="Введите свою почту"
          placeholder="Например, alex@teamsync.ru"
          :model-value="this.email.value"
          :error-message="this.email.errorMessage"
          :is-error="this.email.isError"
          input-type="email"
          :is-input-required="true"
          @update="(value) => this.email.value = value"
      />

      <text-input
          label="Введите свое имя"
          placeholder="Например, Александр"
          :model-value="this.firstName.value"
          :error-message="this.firstName.errorMessage"
          :is-error="this.firstName.isError"
          input-type="text"
          :is-input-required="true"
          @update="(value) => this.firstName.value = value"
      />

      <text-input
          label="Введите свою фамилию"
          placeholder="Например, Иванов"
          :model-value="this.lastName.value"
          :error-message="this.lastName.errorMessage"
          :is-error="this.lastName.isError"
          input-type="text"
          :is-input-required="true"
          @update="(value) => this.lastName.value = value"
      />

      <div class="form-control w-full max-w-xs mt-5">
        <button
            class="btn btn-success"
            :class="this.empty.isEmailEmpty || this.empty.isFirstNameEmpty || this.empty.isLastNameEmpty ? 'btn-outline' : ''"
            @click="checkValues"
        >
          Продолжить
          <ArrowRightCircleIcon class="h-6 w-6"/>
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
import TextInput from "@/components/ui/TextInput.vue";
import {ArrowRightCircleIcon, ArrowRightOnRectangleIcon, PlusCircleIcon} from "@heroicons/vue/24/outline";
import fireImage from "/src/static/fire-dynamic-color-png.png"
import * as EmailValidator from "email-validator";
import TeamsyncButton from "@/components/ui/TeamsyncButton.vue";
import StickyBottomElement from "@/components/ui/StickyBottomElement.vue";

export default {
  components: {
    StickyBottomElement,
    PlusCircleIcon,
    TeamsyncButton,
    ArrowRightCircleIcon, TextInput, ArrowRightOnRectangleIcon
  },
  data() {
    return {
      firstName: {
        value: '',
        isError: false,
        errorMessage: ''
      },
      lastName: {
        value: '',
        isError: false,
        errorMessage: ''
      },
      email: {
        value: '',
        isError: false,
        errorMessage: ''
      },
      empty: {
        isEmailEmpty: true,
        isFirstNameEmpty: true,
        isLastNameEmpty: true
      },

      fire: fireImage
    }
  },
  watch: {
    'email.value': function (value) {
      this.email.value = value;
      if (this.email.value !== '') {
        this.empty.isEmailEmpty = false;
      }
    },
    'firstName.value': function (value) {
      this.firstName.value = value;
      if (this.firstName.value !== '') {
        this.empty.isFirstNameEmpty = false
      }
    },
    'lastName.value': function (value) {
      this.lastName.value = value;
      if (this.lastName.value !== '') {
        this.empty.isLastNameEmpty = false
      }
    }
  },
  methods: {
    checkValues() {
      if (this.email.value === '') {
        this.email.isError = true;
        this.email.errorMessage = "Пожалуйста, укажите свою почту"

        this.firstName.isError = false
        this.lastName.isError = false
      } else if (!EmailValidator.validate(this.email.value)) {
        this.email.isError = true;
        this.email.errorMessage = "Некорректный адрес электронной почты";

        this.firstName.isError = false
        this.lastName.isError = false
      } else if (this.firstName.value === '') {
        this.firstName.isError = true;
        this.firstName.errorMessage = "Пожалуйста, укажите свое имя";

        this.email.isError = false
        this.lastName.isError = false
      } else if (this.lastName.value === '') {
        this.lastName.isError = true;
        this.lastName.errorMessage = "Пожалуйста, укажите свою фамилию";

        this.email.isError = false
        this.firstName.isError = false
      } else {
        this.$router.push({
          name: 'finish_signup_page', query: {
            email: this.email.value,
            firstName: this.firstName.value,
            lastName: this.lastName.value
          }
        })
      }
    }
  }
}
</script>