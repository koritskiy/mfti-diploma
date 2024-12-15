<template>
    <form @submit.prevent method="post">
        <text-input
                label="Название категории"
                placeholder="Например, Java-задачи"
                v-model:model-value="category.name"
        />

        <div class="flex justify-center">
            <success-outline-button @click="createCategory" class="">Создать</success-outline-button>
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
            category: {
                name: ''
            }
        }
    },
    methods: {
        createCategory() {
            this.$emit('create', this.category)
        }
    }
}
</script>