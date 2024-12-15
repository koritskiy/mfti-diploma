<template>
    <form @submit.prevent method="post">
        <textarea-element placeholder="Введите сюда Ваш токен" v-model:model-value="token" />

        <div class="flex justify-center">
            <success-outline-button @click="uploadToken" class="">Загрузить токен</success-outline-button>
        </div>
    </form>
</template>
<script>
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";
import TextareaElement from "@/components/ui/TextareaElement.vue";

export default {
    components: {TextareaElement, SuccessOutlineButton},
    data() {
        return {
            token: ''
        }
    },
    methods: {
        uploadToken() {
            this.$emit('create', this.token)
        }
    }
}
</script>