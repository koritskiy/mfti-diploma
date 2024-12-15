<template>
    <form @submit.prevent method="post">
        <text-input
                label="Название доски"
                placeholder="Например, Проект Х"
                v-model:model-value="board.name"
        />

        <div class="flex justify-center">
            <success-outline-button @click="createBoard" class="">Создать</success-outline-button>
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
            board: {
                name: ''
            }
        }
    },
    methods: {
        createBoard() {
            this.$emit('create', this.board)
        }
    }
}
</script>