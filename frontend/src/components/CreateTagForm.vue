<template>
    <form @submit.prevent method="post">
        <h2 class="mb-5 mt-5 text-2xl font-bold text-base-color text-center"> Создать тег </h2>
        <text-input
                label="Название тега"
                placeholder="Например, бекенд"
                v-model:model-value="tag.name"
        />

        <text-input
                label="Описание тега"
                placeholder="Например, задачи для бекенд-сервисов"
                v-model:model-value="tag.description"
        />

        <div class="flex justify-center">
            <success-outline-button @click="createTag" class="">Создать тег</success-outline-button>
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
            tag: {
                name: '',
                description: ''
            }
        }
    },
    methods: {
        createTag() {
            this.$emit('create', this.tag)
        }
    }
}
</script>