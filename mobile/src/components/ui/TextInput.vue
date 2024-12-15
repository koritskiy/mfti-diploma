<template>
  <div class="form-control w-full max-w-xs mt-3">
    <label class="label">
      <span class="label-text">
        {{ label }}
      </span>
    </label>
    <input
        :value="modelValue"
        @input="updateInput"
        :type="inputType"
        :placeholder="placeholder"
        class="input input-bordered w-full max-w-xs placeholder-base-content placeholder-opacity-40"
        :class="isError ? 'input-error' : ''"
        :required="isInputRequired"
        :readonly="isInputReadonly"
        :disabled="isInputDisabled"
    />

    <Transition name="label">
      <label
          class="label"
          v-if="isError && errorMessage"
      >
        <span class="label-text-alt text-error">{{ errorMessage }}</span>
      </label>
    </Transition>
  </div>
</template>
<script>
export default {
  name: 'text-input',
  props: {
    label: {
      type: String,
      required: true
    },
    placeholder: {
      type: String,
      required: true
    },
    modelValue: {
      type: String,
      required: true
    },
    inputType: {
      type: String,
      default: "text",
      required: false
    },
    isError: {
      type: Boolean,
      required: true
    },
    errorMessage: {
      type: String,
      required: false
    },
    isInputRequired: {
      type: Boolean,
      default: false
    },
    isInputReadonly: {
      type: Boolean,
      default: false
    },
    isInputDisabled: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    updateInput(event) {
      this.$emit('update', event.target.value)
    }
  }
}
</script>

<style>
.label-enter-active,
.label-leave-active {
  transition: opacity 0.5s ease;
}

.label-enter-from,
.label-leave-to {
  opacity: 0;
}
</style>