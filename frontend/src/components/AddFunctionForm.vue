<template>
    <form @submit.prevent method="post">
        <h2 class="mb-5 mt-5 text-2xl font-bold text-base-color text-center"> Создать должность </h2>
        <text-input
                label="Название должности"
                placeholder="Например, разработчик"
                v-model:model-value="func.name"
        />

        <div class="flex justify-center">
            <success-outline-button @click="createFunction" class="">Добавить должность</success-outline-button>
        </div>
    </form>
</template>

<script>
import TextInput from "@/components/ui/TextInput.vue";
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";

export default {
    components: {SuccessOutlineButton, TextInput},
    data() {
        return {
            func: {
                name: ''
            }
        }
    },
    methods: {
        createFunction() {
            this.$emit('create', this.func)
        }
    }
}
</script>