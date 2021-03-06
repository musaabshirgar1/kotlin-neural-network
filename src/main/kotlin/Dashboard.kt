import javafx.application.Application
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import model.predictor.FontShade
import model.predictor.LabeledColor
import model.predictor.PredictorModel
import model.predictor.randomColor
import tornadofx.*


fun main() = Application.launch(MainApp::class.java)

class MainApp : App(MainView::class)

class MainView : View() {

    private val backgroundColor = SimpleObjectProperty(Color.GRAY)

    private fun assignRandomColor() = randomColor()
        .also {
            backgroundColor.set(it)
        }

    override val root = splitpane {
        style = "-fx-font-size: 16pt; "
        orientation = Orientation.VERTICAL

        splitpane {

            title = "Light/Dark Text Suggester"
            orientation = Orientation.HORIZONTAL

            borderpane {

                top = label("TRAIN") {
                    style {
                        textFill = Color.RED
                        fontWeight = FontWeight.BOLD
                    }
                }

                center = form {
//                    fieldset {
//
//                        field("Which looks better?").hbox {
//                            button("DARK") {
//                                textFill = Color.BLACK
//                                useMaxWidth = true
//
//                                backgroundProperty().bind(
//                                    backgroundColor.select {
//                                        ReadOnlyObjectWrapper(
//                                            Background(
//                                                BackgroundFill(
//                                                    it,
//                                                    CornerRadii.EMPTY,
//                                                    Insets.EMPTY
//                                                )
//                                            )
//                                        )
//                                    }
//                                )
//
//                                action {
//                                    runAsyncWithProgress {
//                                        PredictorModel += LabeledColor(
//                                            color = backgroundColor.get(),
//                                            fontShade = FontShade.DARK
//                                        )
//                                    }
//                                    assignRandomColor()
//                                }
//                            }
//
//                            button("LIGHT") {
//                                textFill = Color.WHITE
//                                useMaxWidth = true
//
//                                backgroundProperty().bind(
//                                    backgroundColor.select {
//                                        ReadOnlyObjectWrapper(
//                                            Background(
//                                                BackgroundFill(
//                                                    it,
//                                                    CornerRadii.EMPTY,
//                                                    Insets.EMPTY
//                                                )
//                                            )
//                                        )
//                                    }
//                                )
//
//                                action {
//                                    runAsyncWithProgress {
//                                        PredictorModel += LabeledColor(
//                                            color = backgroundColor.get(),
//                                            fontShade = FontShade.LIGHT
//                                        )
//                                    }
//                                    assignRandomColor()
//                                }
//                            }
//                        }
//                    }
                    fieldset {
                        field("Model") {
                            combobox(PredictorModel.selectedPredictor) {
                                println("Default Value: ${PredictorModel.selectedPredictor.value}")
                                PredictorModel.Predictor.values().forEach {
                                    items.add(it)
                                }
                            }
                        }
                    }
                    fieldset {
                        field("Pre-Train") {
                            button("Train 1345 Colors") {
                                useMaxWidth = true
                                action {
                                    runAsyncWithProgress {
                                        println("Pre Training Button Clicked with ${PredictorModel.selectedPredictor.value} Selected")
                                        PredictorModel.preTrainData()
                                        isDisable = true
                                    }
                                }
                            }
                        }
                    }
                }
            }

            borderpane {
                top = label("PREDICT") {
                    style {
                        textFill = Color.RED
                        fontWeight = FontWeight.BOLD
                    }
                }
                center = form {
                    fieldset {
                        field("Background") {
                            colorpicker {
                                valueProperty().onChange {
                                    backgroundColor.set(it)
                                }
                                customColors.forEach {
                                    println(it)
                                }
                            }
                        }
                        field("Result") {
                            label("LOREM IPSUM") {
                                backgroundProperty().bind(
                                    backgroundColor.select {
                                        ReadOnlyObjectWrapper(
                                            Background(
                                                BackgroundFill(
                                                    it,
                                                    CornerRadii.EMPTY,
                                                    Insets.EMPTY
                                                )
                                            )
                                        )
                                    }
                                )
                                backgroundColor.onChange {
                                    val result = PredictorModel.predict(it!!)
                                    text = result.toString()
                                    textFill = result.color
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
